# XMG

XMG (X11 Mouse Grabber) is a Linux/X11 tool for intercepting mouse
button press events and triggering actions.

It's a way of making use of the extra mouse buttons when the manufacturer
does not provide Linux drivers and/or tools for configuring button actions
(which means 99.9% of the cases).

## Installation

1. Install [libxcb](https://xcb.freedesktop.org/). In most distributions
it's possible to install it through the package manager.

2. Download the [latest release](https://github.com/edrd-f/xmg/releases) and
	 place it under `/usr/local/bin` or other executable location.

3. Create a [configuration file](#configuration).

4. Execute `xmg` passing as argument the relative or absolute path to the
	 configuration file. For example:

    ```sh
    xmg ~/.config/xmg.toml
    ```

5. (optional) Create an autostart configuration. For KDE environments, this
can be a simple script in `~/.config/autostart-scripts`.

## Configuration

Configuration files are written in [TOML](https://toml.io/en/) format. The following example describes all supported options:

```toml
version = 1

[[mappings]]
  button = 8
  command = '''
    qdbus org.kde.kglobalaccel
      /component/kwin invokeShortcut "ExposeAll"
  '''

[[mappings]]
  button = 9
  command = '''
    dbus-send
      --type=method_call
      --dest=org.mpris.MediaPlayer2.spotify
      /org/mpris/MediaPlayer2
      org.mpris.MediaPlayer2.Player.PlayPause
  '''
```

You can find out the button numbers by using `xinput`, from `xorg-xinput` package:

```sh
xinput test-xi2 --root | grep -A 10 ButtonPress
```

The number after `detail` is the button number.

## Debugging

You can check XMG is receiving mouse events correctly by looking at debug logs. To activate them, set the `LogLevel` environment variable to `debug`:

```sh
LogLevel=debug xmg config.toml
```

## Future ideas

* Configurations per application
* Long press actions
