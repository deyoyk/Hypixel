load("@prelude//java:java_toolchain.bzl", "AbiGenerationMode", "JavaPlatformInfo", "JavaToolchainInfo")

def _local_java_toolchain_impl(ctx):
    return [
        DefaultInfo(),
        JavaPlatformInfo(
            name = ctx.attrs.name,
        ),
        JavaToolchainInfo(
            abi_generation_mode = AbiGenerationMode("none"),
            compile_and_package = ctx.attrs.compile_and_package,
            fat_jar = ctx.attrs.fat_jar,
            fat_jar_main_class_lib = ctx.attrs.fat_jar_main_class_lib,
            gen_class_to_source_map = ctx.attrs.gen_class_to_source_map,
            gen_class_to_source_map_include_sourceless_compiled_packages = ctx.attrs.gen_class_to_source_map_include_sourceless_compiled_packages,
            global_code_config = {},
            is_bootstrap_toolchain = True,
            jar = ctx.attrs.jar[RunInfo] if ctx.attrs.jar else None,
            jar_builder = RunInfo(cmd_args([ctx.attrs.java[RunInfo], "-jar", ctx.attrs.jar_builder])),
            java = ctx.attrs.java,
            java_for_tests = ctx.attrs.java_for_tests[RunInfo] if ctx.attrs.java_for_tests else ctx.attrs.java[RunInfo],
            javac = ctx.attrs.javac,
            javac_protocol = "classic",
            jlink = ctx.attrs.jlink,
            jmod = ctx.attrs.jmod,
            jrt_fs_jar = ctx.attrs.jrt_fs_jar,
            merge_class_to_source_maps = ctx.attrs.merge_class_to_source_maps,
            source_level = ctx.attrs.source_level,
            src_root_elements = [],
            src_root_prefixes = [],
            target_level = ctx.attrs.target_level,
            track_class_usage = False,
            zip_scrubber = RunInfo(cmd_args([ctx.attrs.java[RunInfo], "-jar", ctx.attrs.zip_scrubber])),
        ),
    ]

local_java_toolchain = rule(
    impl = _local_java_toolchain_impl,
    is_toolchain_rule = True,
    attrs = {
        "compile_and_package": attrs.dep(default = "prelude//java/tools:compile_and_package"),
        "fat_jar": attrs.dep(default = "prelude//java/tools:fat_jar"),
        "fat_jar_main_class_lib": attrs.option(attrs.source(), default = None),
        "gen_class_to_source_map": attrs.exec_dep(
            default = "prelude//java/tools:gen_class_to_source_map",
            providers = [RunInfo],
        ),
        "gen_class_to_source_map_include_sourceless_compiled_packages": attrs.list(attrs.string(), default = ["androidx.databinding"]),
        "jar": attrs.option(attrs.dep(providers = [RunInfo]), default = None),
        "jar_builder": attrs.source(default = "root//tools/buck-java-tools:jar_builder_main.jar"),
        "java": attrs.exec_dep(),
        "java_for_tests": attrs.option(attrs.dep(providers = [RunInfo]), default = None),
        "javac": attrs.option(attrs.one_of(attrs.dep(), attrs.source(), attrs.string()), default = None),
        "jlink": attrs.exec_dep(),
        "jmod": attrs.exec_dep(),
        "jrt_fs_jar": attrs.source(),
        "merge_class_to_source_maps": attrs.exec_dep(
            default = "prelude//java/tools:merge_class_to_source_maps",
            providers = [RunInfo],
        ),
        "source_level": attrs.string(default = "25"),
        "target_level": attrs.string(default = "25"),
        "zip_scrubber": attrs.source(default = "root//tools/buck-java-tools:zip_scrubber_main.jar"),
    },
)
