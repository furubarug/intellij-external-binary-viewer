# IntelliJ External Binary Viewer

[![Version](https://img.shields.io/jetbrains/plugin/v/18695-external-binary-viewer.svg)](https://plugins.jetbrains.com/plugin/18695-external-binary-viewer)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/18695-external-binary-viewer.svg)](https://plugins.jetbrains.com/plugin/18695-external-binary-viewer)
[![License: MIT](https://img.shields.io/github/license/furubarug/intellij-external-binary-viewer)](https://github.com/furubarug/intellij-external-binary-viewer/blob/main/LICENSE)

<!-- Plugin description -->
[External Binary Viewer](https://plugins.jetbrains.com/plugin/18695-external-binary-viewer) provides the viewing of
binary files by external command text conversion. This works
like [textconv](http://git-scm.com/docs/gitattributes/2.32.0#_performing_text_diffs_of_binary_files) in Git.

### Usage

1. <kbd>Settings/Preferences</kbd> > <kbd>External Binary Viewer</kbd> > <kbd>+</kbd>
2. Enter the extension of the binary file and the command for converting it to text.

### Use cases

* View a docx file by converting it to Markdown format with [Pandoc](https://github.com/jgm/pandoc).

| Extensions | Command                           | Converted Extension |
|:-----------|:----------------------------------|:--------------------|
| docx       | `pandoc -f docx -t markdown`      | md                  |

* View differences in tar files.

| Extensions | Command  | Converted Extension |
|:-----------|:---------|:--------------------|
| tar        | `tar -t` | txt                 |

<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "External Binary
  Viewer"</kbd> >
  <kbd>Install Plugin</kbd>

- Manually:

  Download the [latest release](https://github.com/furubarug/intellij-external-binary-viewer/releases/latest) and
  install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
