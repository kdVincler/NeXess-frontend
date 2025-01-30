# NeXess - Frontend

The frontend for my final year project during BSc Computer Science at Queen Mary University of London. An NFC based access control application.

## Project Hardware Requirements

The project requires the following hardware:

- an NFC capable Android phone running **Android 7.0 (Nougat, API 24)** or higher, with an NFC tag utility application installed (developed using the [NFC Tools app](https://play.google.com/store/apps/details?id=com.wakdev.wdnfc&hl=en_GB))
- a laptop/computer with Android Studio installed
- writeable NFC tag(s) (developed using Mifare Classic NfcA tags)

> ## Disclaimer
>
> **This project works on your local network, using your laptop's current IP address as a server URL. It needs both the phone and machine running the server to be connected to the same WiFi network**
>
> **Also take note that eduroam and similar WiFi networks will not allow the traffic the project generates. If these kind of networks are the only available network around, use the mobile network hotspot of a different mobile phone insted.**

## Setup instructions

To run & test this project on your own machine, ensure [the backend server](https://github.com/kdVincler/NeXess_backend) is installed and running on your machine and that your android device and machine are on the same WiFi network then follow these steps:

1. Donwload or clone this repository

2. In the root directory of this repository (i.e. the same folder this README file is in) create a file called `env.properties` and in it declare & set a variable called `IP_ADDRESS` to your laptop's current IP address as shown below (can be achieved with opening the created `env.properties` file in any text editor)

   ```kotlin
   IP_ADDRESS="YOUR_IP_ADDRESS_HERE"
   ```

3. Open the downloaded/cloned folder in Android Studio and wait for it to index the project

4. On your phone Enable Developer options and USB debugging

   - **Developer options:** Settings -> About phone -> tap Build number 7 times
   - **USB debugging:** Settings > Developer options > enable USB debugging

5. Connect your phone to your machine and select file transfer on the phone when prompted

6. Install application to your phone

   - In Android Studio, select the connected device from the available devices dropdown.
   - Then, hit the green Run button
   - After building is done, an installation screen should appear on your phone. Select Allow when asked about allowing third party/hardware installation and click Install on the subsequent screen

7. The app should automatically start running after installation. You can disconnect your device from the machine and can use the phone and app normally.

### A note on the `IP_ADDRESS` environmental variable.

Every time your IP address changes (i.e. you try to run the project on a different network then when local setup happened), you need to edit the value of `IP_ADDRESS` in `env.properties`, save the file, and then clean, rebuild and rerun/reinstall the new version of the application.
Always ensure the environmental variable is set to the correct value, otherwise the project will not work.

- **Clean build:** In Android Studio from the toolbar select Build > Clean Project and wait for it to finish
- **Rebuild:** In Android Studio from the toolbar select Build > Rebuild Project and wait for it to finish
- **Reinstall:** Steps 5 and 6 of Setup instructions

## Usage

- **Account door and permission creation detailed in [the backend server's](https://github.com/kdVincler/NeXess_backend) usage section**

- To enable the "door opening" feature of the project:

  0. Create a superuser (or if superuser was already created optionally create more users) and assign higher level permissions as needed (default permission is level 0)

  1. Create as many door entries as needed

  2. If the used NFC tags contain data, erase it using the NFC Tools application

  3. Using the NFC Tools application (or any other NFC tag utility application), write a single Text (text/plain) record containing the ID number which you can find on the overview of the Door table

- After launching the app, there is a splash screen that checks with the server (using the cookies persisted) if the user is logged in/autherised. If they are, the application navigates to the reader screen, if they are not, the login screen is shown

- Doors can be "opened" through scanning NFC tags while on the reader screen (NFC scanning is active while the indicator is shown on screen)

- The log screen shows the successful and unsuccessful door openings. This screen supports pull down refresh to update the user's logs

- The profile screen gives the basic information of the user and facilitates logout
