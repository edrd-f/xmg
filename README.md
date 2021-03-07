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
    xmg ~/.config/xmg.properties
    ```

5. (optional) Create an autostart configuration. For KDE environments, this
can be a simple script in `~/.config/autostart-scripts`.

## Configuration (TODO: update)

The configuration file must use the properties* format
(`key = value`), where `key` should be `button.{number}.command = {command}`.
For example:

```properties
# Shell commands are ok
button.8.command = echo Hello

# Spotify Play/Pause through D-Bus
button.9.command = dbus-send --type=method_call \
	--dest=org.mpris.MediaPlayer2.spotify \
	/org/mpris/MediaPlayer2 \
	org.mpris.MediaPlayer2.Player.PlayPause
```

\* _Only a subset of the properties format is supported._

You can find out what are the button numbers by using `xinput`, from `xorg-xinput` package:

```sh
xinput test-xi2 --root | grep -A 10 ButtonPress
```

The number after `detail` is the button number.

## Future ideas

* Configurations per application
* Long press actions
