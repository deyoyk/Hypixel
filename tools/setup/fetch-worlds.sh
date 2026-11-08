#!/usr/bin/env bash
set -euo pipefail

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
DEST="$REPO_ROOT/config/game"
URL="${SKYBLOCK_WORLD_ASSETS_URL:-https://files.catbox.moe/flri48.zip}"
EXPECTED_SHA="${SKYBLOCK_WORLD_ASSETS_SHA:-976A92B5AB826462D6E4BFF039D52B60F31D384A8F56B2CCED73712095E35E57}"

tmp="$(mktemp -d)"
trap 'rm -rf "$tmp"' EXIT

echo "Downloading world assets from $URL ..."
curl -fSL --retry 3 -o "$tmp/assets.zip" "$URL"

if command -v sha256sum >/dev/null 2>&1; then
  actual="$(sha256sum "$tmp/assets.zip" | awk '{print $1}')"
  if [[ "${actual^^}" != "${EXPECTED_SHA^^}" ]]; then
    echo "SHA-256 mismatch: got $actual, expected $EXPECTED_SHA" >&2
    exit 1
  fi
  echo "Checksum OK."
fi

echo "Extracting into $DEST ..."
python3 - "$tmp/assets.zip" "$DEST" <<'EOF'
import sys, zipfile
src, dest = sys.argv[1], sys.argv[2]
keep = ("world/", "limbo.schem", "resourcepacks/",
        "bedwars/", "murdermystery/", "skywars/", "ravengard/dungeon_1.polar")
with zipfile.ZipFile(src) as z:
    for name in z.namelist():
        if name.endswith("/") or not name.startswith(keep):
            continue
        z.extract(name, dest)
print("done")
EOF

echo "World assets ready in $DEST"
