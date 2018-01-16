package com.disusered;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;

import android.net.Uri;
import android.content.Intent;
import android.webkit.MimeTypeMap;
import android.content.ActivityNotFoundException;
import android.os.Build;
import android.support.v4.content.FileProvider;
import java.io.File;
import android.content.Context;

/**
 * This class starts an activity for an intent to view files
 */
public class Open extends CordovaPlugin {

  public class GenericFileProvider extends FileProvider {
    // Extending the FileProvider class to avoid problems with the AndroidManifest.mxl file
  }

  public static final String OPEN_ACTION = "open";

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    if (action.equals(OPEN_ACTION)) {
      String path = args.getString(0);
      this.chooseIntent(path, callbackContext);
      return true;
    }
    return false;
  }

  /**
   * Returns the MIME type of the file.
   *
   * @param path
   * @return
   */
  private static String getMimeType(String path) {
    String mimeType = null;

    String extension = MimeTypeMap.getFileExtensionFromUrl(path);
    if (extension != null) {
      MimeTypeMap mime = MimeTypeMap.getSingleton();
      String ext = path.substring(path.lastIndexOf(".") + 1);
      mimeType = mime.getMimeTypeFromExtension(ext);
    }

    System.out.println("Mime type: " + mimeType);

    return mimeType;
  }

  /**
   * Creates an intent for the data of mime type
   *
   * @param path
   * @param callbackContext
   */
  private void chooseIntent(String path, CallbackContext callbackContext) {

    Context currentContext = this.cordova.getActivity().getApplicationContext();

    if (path != null && path.length() > 0) {
      // Getting rid of uri prefix
      path = path.replaceFirst("file://", "");
      path = path.replaceFirst("file:/", "");

      Uri uri = null;

      // The 'uri' variable recieves a value based on the current SDK
      if(Build.VERSION.SDK_INT >= 24){
        File file = new File(path);
        uri = GenericFileProvider.getUriForFile(currentContext, currentContext.getPackageName() + ".provider", file);
      } else {
        uri = Uri.parse("file://" + path);
      }

      try {
        String mime = getMimeType(path);
        Intent fileIntent = new Intent(Intent.ACTION_VIEW);
        // Important to add, else apps that are trying to open the file would crash
        fileIntent.addFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION );

        if( Build.VERSION.SDK_INT > 15 ){
          fileIntent.setDataAndTypeAndNormalize(uri, mime); // API Level 16 -> Android 4.1
        } else {
          fileIntent.setDataAndType(uri, mime);
        }

        cordova.getActivity().startActivity(fileIntent);

        callbackContext.success();
      } catch (ActivityNotFoundException e) {
        e.printStackTrace();
        callbackContext.error(1);
      }
    } else {
      callbackContext.error(2);
    }
  }
}
