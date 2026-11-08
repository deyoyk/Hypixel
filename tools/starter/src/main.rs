use std::collections::HashSet;
use std::path::{Path, PathBuf};
use std::process::Command;

const INFRA: [&str; 5] = ["mongodb", "redis", "proxy", "pico_limbo", "pack_server"];
const ALWAYS_SERVER: &str = "hypixelcore_main_lobby";
const CORE_SERVICES: [&str; 6] = [
    "service_api",
    "service_auctionhouse",
    "service_bazaar",
    "service_itemtracker",
    "service_party",
    "service_store",
];
const PRESETS: [&str; 6] = ["skyblock", "bedwars", "skywars", "murdermystery", "full", "custom"];

fn preset_servers(name: &str) -> Vec<&'static str> {
    match name {
        "skyblock" => vec![
            "hypixelcore_island",
            "hypixelcore_hub",
            "hypixelcore_spiders_den",
            "hypixelcore_the_end",
            "hypixelcore_crimson_isle",
            "hypixelcore_dungeon_hub",
            "hypixelcore_farming",
            "hypixelcore_gold_mine",
            "hypixelcore_deep_caverns",
            "hypixelcore_dwarven_mines",
            "hypixelcore_the_park",
            "hypixelcore_galatea",
            "hypixelcore_backwater_bayou",
            "hypixelcore_jerrys_workshop",
        ],
        "bedwars" => vec![
            "hypixelcore_bedwars_lobby",
            "hypixelcore_bedwars_game",
            "hypixelcore_bedwars_configurator",
        ],
        "skywars" => vec![
            "hypixelcore_skywars_lobby",
            "hypixelcore_skywars_game",
            "hypixelcore_skywars_configurator",
        ],
        "murdermystery" => vec![
            "hypixelcore_murder_mystery_lobby",
            "hypixelcore_murder_mystery_game",
            "hypixelcore_murder_mystery_configurator",
        ],
        _ => vec![],
    }
}

fn repo_root() -> PathBuf {
    let mut dir = std::env::current_exe()
        .ok()
        .and_then(|p| p.parent().map(|p| p.to_path_buf()))
        .unwrap_or_else(|| std::env::current_dir().expect("no working directory"));
    loop {
        if dir.join("docker-compose.yml").exists() {
            return dir;
        }
        if !dir.pop() {
            eprintln!("docker-compose.yml not found above the starter binary");
            std::process::exit(1);
        }
    }
}

fn docker_ok(root: &Path) -> bool {
    Command::new("docker")
        .args(["info"])
        .current_dir(root)
        .output()
        .map(|o| o.status.success())
        .unwrap_or(false)
}

fn compose_services(root: &Path) -> Vec<String> {
    let out = Command::new("docker")
        .args(["compose", "config", "--services"])
        .current_dir(root)
        .output()
        .expect("failed to run docker compose");
    if !out.status.success() {
        eprintln!("docker compose config failed");
        std::process::exit(1);
    }
    String::from_utf8_lossy(&out.stdout)
        .lines()
        .map(|l| l.trim().to_string())
        .filter(|l| !l.is_empty())
        .collect()
}

fn pick_multi(title: &str, items: &[String]) -> Vec<String> {
    match inquire::MultiSelect::new(title, items.to_vec())
        .with_help_message("Space to select, Enter to confirm")
        .with_page_size(12)
        .prompt()
    {
        Ok(v) => v,
        Err(_) => std::process::exit(0),
    }
}

fn dedupe(items: Vec<String>) -> Vec<String> {
    let mut seen = HashSet::new();
    items
        .into_iter()
        .filter(|s| seen.insert(s.clone()))
        .collect()
}

fn resolve(available: &[String], presets: &[String], custom_servers: Vec<String>, custom_services: Vec<String>) -> (Vec<String>, Vec<String>) {
    let mut servers = vec![ALWAYS_SERVER.to_string()];
    let mut services: Vec<String> = vec![];
    if presets.iter().any(|p| p == "full") {
        servers.extend(
            available
                .iter()
                .filter(|s| s.starts_with("hypixelcore_") || s.as_str() == "prototype_lobby")
                .cloned(),
        );
        services.extend(
            available
                .iter()
                .filter(|s| s.starts_with("service_"))
                .cloned(),
        );
    } else {
        for p in presets {
            if p == "custom" {
                continue;
            }
            servers.extend(preset_servers(p).iter().map(|s| s.to_string()));
            services.extend(CORE_SERVICES.iter().map(|s| s.to_string()));
        }
        servers.extend(custom_servers);
        services.extend(custom_services);
    }
    let known: HashSet<&str> = available.iter().map(|s| s.as_str()).collect();
    servers.retain(|s| known.contains(s.as_str()));
    services.retain(|s| known.contains(s.as_str()));
    servers.sort();
    services.sort();
    (dedupe(servers), dedupe(services))
}

fn boot(root: &Path, servers: &[String], services: &[String]) {
    let mut all: Vec<String> = INFRA.iter().map(|s| s.to_string()).collect();
    all.extend(servers.iter().cloned());
    all.extend(services.iter().cloned());
    let all = dedupe(all);
    println!("Starting: {}", all.join(", "));
    let mut args = vec!["compose", "up", "-d", "--build"];
    args.extend(all.iter().map(|s| s.as_str()));
    let status = Command::new("docker")
        .args(&args)
        .current_dir(root)
        .status()
        .expect("failed to run docker compose up");
    if !status.success() {
        std::process::exit(1);
    }
    let _ = Command::new("docker")
        .args(["compose", "ps"])
        .current_dir(root)
        .status();
    println!("Follow logs with: docker compose logs -f <service>");
}

fn main() {
    let root = repo_root();
    if !docker_ok(&root) {
        eprintln!("Docker is not running. Start Docker Desktop and try again.");
        std::process::exit(1);
    }
    let available = compose_services(&root);

    let cli: Vec<String> = std::env::args().skip(1).collect();
    if cli.first().map(|s| s.as_str()) == Some("--dry-run") {
        let presets: Vec<String> = cli[1..].to_vec();
        let (servers, services) = resolve(&available, &presets, vec![], vec![]);
        println!("servers: {}", servers.join(","));
        println!("services: {}", services.join(","));
        return;
    }

    let preset_items: Vec<String> = PRESETS.iter().map(|s| s.to_string()).collect();
    let picked = pick_multi("Select presets", &preset_items);

    let (custom_servers, custom_services) = if picked.iter().any(|p| p == "custom") {
        let mut servers: Vec<String> = available
            .iter()
            .filter(|s| s.starts_with("hypixelcore_") || s.as_str() == "prototype_lobby")
            .cloned()
            .collect();
        servers.sort();
        let mut backends: Vec<String> = available
            .iter()
            .filter(|s| s.starts_with("service_"))
            .cloned()
            .collect();
        backends.sort();
        (
            pick_multi("Select game servers to start", &servers),
            pick_multi("Select backend services to start", &backends),
        )
    } else {
        (vec![], vec![])
    };

    let (servers, services) = resolve(&available, &picked, custom_servers, custom_services);
    println!("Servers ({}): {}", servers.len(), servers.join(", "));
    println!("Services ({}): {}", services.len(), services.join(", "));
    let go = inquire::Confirm::new("Start these?")
        .with_default(true)
        .prompt()
        .unwrap_or(false);
    if !go {
        return;
    }
    boot(&root, &servers, &services);
}
