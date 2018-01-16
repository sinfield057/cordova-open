cordova-open
====

Disclosure:
I have never developed in Java or Android before. This is just a small modification to the original repository so that it would work with Android SDK versions greater than 24. The change is also targeted at a specific need in a different project. I do not guarantee that it will work in absolutely all cases. Use at your own peril. Or modify it yourself, which would be even better.

Make sure the following lines are present in your AndroidManifest.xml file:
```javascript
<provider android:authorities="${applicationId}.provider" android:exported="false" android:grantUriPermissions="true" android:name="android.support.v4.content.FileProvider">
      <meta-data android:name="android.support.FILE_PROVIDER_PATHS" android:resource="@xml/provider_paths"/>
</provider>

<provider android:authorities="${applicationId}.provider" android:exported="false" android:grantUriPermissions="true" android:name=".GenericFileProvider">
      <meta-data android:name="android.support.FILE_PROVIDER_PATHS" android:resource="@xml/provider_paths"/>
</provider>
```


This repository is a fork of https://github.com/disusered/cordova-open.


## Install

```bash
$ cordova plugin add https://github.com/sinfield057/cordova-open.git
```

## Usage

```javascript
var open = cordova.plugins.disusered.open;

function success() {
  console.log('Success');
}

function error(code) {
  if (code === 1) {
    console.log('No file handler found');
  } else {
    console.log('Undefined error');
  }
}

function progress(progressEvent) {
  if (progressEvent.lengthComputable) {
    var perc = Math.floor(progressEvent.loaded / progressEvent.total * 100);
    // update UI with status, for example:
    // statusDom.innerHTML = perc + "% loaded...";
  } else {
    // download does not offer a length... just show dots
    /*
       if(statusDom.innerHTML == "") {
       statusDom.innerHTML = "Loading";
       } else {
       statusDom.innerHTML += ".";
       }
     */
  }
};

open('file:/storage/sdcard/DCIM/Camera/1404177327783.jpg', success, error, progress);
```

## API
The plugin exposes the following methods:

```javascript
cordova.plugins.disusered.open(file, success, error, progress, trustAllCertificates)
```

#### Parameters:
* __file:__ A string representing a URI
* __success:__ Optional success callback
* __error:__ Optional error callback
* __progress:__ Optional progress callback
* __trustAllCertificates:__ Optional, trusts any certificate when the connection is done over HTTPS.

#### Events:
* __pause:__ Opening files emits Cordova's pause event (Android only)
* __resume:__ Closing the file emits Cordova's resume event
* __open.success:__ File is found and can be opened
* __open.error:__ File not found, or no file handler is installed

## License

MIT © [Carlos Rosquillas](http://carlosanton.io)
