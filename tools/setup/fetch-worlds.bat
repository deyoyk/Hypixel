@echo off
setlocal
set "URL=https://files.catbox.moe/flri48.zip"
if defined SKYBLOCK_WORLD_ASSETS_URL set "URL=%SKYBLOCK_WORLD_ASSETS_URL%"
set "DEST=%~dp0..\..\config\game"
set "TMP=%TEMP%\skyblock-world-assets.zip"

echo Downloading world assets from %URL% ...
curl.exe -fSL --retry 3 -o "%TMP%" "%URL%" || exit /b 1

echo Extracting into %DEST% ...
powershell -NoProfile -ExecutionPolicy Bypass -Command ^
  "Add-Type -AssemblyName System.IO.Compression.FileSystem; " ^
  "$z=[System.IO.Compression.ZipFile]::OpenRead('%TMP%'); " ^
  "foreach($e in $z.Entries){ " ^
  "  $n=$e.FullName; " ^
  "  if($n.EndsWith('/')){continue}; " ^
  "  if(-not ($n.StartsWith('world/') -or $n -eq 'limbo.schem' -or $n.StartsWith('resourcepacks/') -or $n.StartsWith('bedwars/') -or $n.StartsWith('murdermystery/') -or $n.StartsWith('skywars/') -or $n -eq 'ravengard/dungeon_1.polar')){continue}; " ^
  "  $t=Join-Path '%DEST%' ($n -replace '/', '\'); " ^
  "  $d=Split-Path $t -Parent; if(-not (Test-Path $d)){New-Item -ItemType Directory -Path $d -Force|Out-Null}; " ^
  "  [System.IO.Compression.ZipFileExtensions]::ExtractToFile($e,$t,$true) }; " ^
  "$z.Dispose()"
del "%TMP%"
echo World assets ready in %DEST%
