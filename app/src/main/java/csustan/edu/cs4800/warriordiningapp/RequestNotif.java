package csustan.edu.cs4800.warriordiningapp;
//notification controller
public class RequestNotif {
    //public static boolean request;
    public static boolean isRequested;
    public boolean activeRequest;

    boolean isRequested(activeRequest){
       if
       (activeRequest = true){
           return true;
       }
       else
       { return false;}


   }

   public static void main(String[] args){

       do {
           //retrieve token
           FirebaseMessaging.getInstance().getToken()
                   .addOnCompleteListener(new OnCompleteListener<String>() {
                       @Override
                       public void onComplete(@NonNull Task<String> task) {
                           if (!task.isSuccessful()) {
                               Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                               return;
                           }

                           // Get new FCM registration token
                           String token = task.getResult();

                           // Log and toast
                           String msg = getString(R.string.msg_token_fmt, token);
                           Log.d(TAG, msg);
                           Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                       }
                   });
        //send notification while isRequested(true)
       } while (isRequested(activeRequest));
   }
    }

