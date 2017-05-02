
    /**
     * return json data from server or error log with response code
     */
    public class AsyncPostDataUrlConnection extends AsyncTask<Void, Void, String> {


        public final static String METHOD_GET = "GET";
        public final static String METHOD_POST = "POST";
        public final static String METHOD_DELETE = "DELETE";
        public final static String METHOD_PATCH = "PATCH";
        public final static String METHOD_PUT = "PUT";

        private String LINK;
        private String METHOD;
        private String postParams;
        private String ResponseData;
        private delegateService delegate;
        private int statusCode;

        /**
         * @param url      -> link of service
         * @param method   -> service method [POST-GET-PUT-DELETE-PATCH]
         * @param delegate -> service response interface
         * @param params   -> body parameter for GET & POST
         */
        public AsyncPostDataUrlConnection(String url, String method, delegateService delegate, KeyValue... params) {
            this.METHOD = method;
            this.LINK = url;
            this.delegate = delegate;
            progressDialog = ProgressDialog.show(context, null,
                    "wait...", false);
            // this.progress.setTitle(null);
            progressDialog.dismiss();
            StringBuilder sb = new StringBuilder();
            for (KeyValue keyValue : params) {
                sb.append(keyValue.getKey()).append("=").append(keyValue.getValue()).append("&");
            }
            postParams = sb.toString();
            if (method.equalsIgnoreCase(METHOD_GET))
                this.LINK += "?" + postParams;
        }
        
    public static boolean isNetworkConnected() {
        ConnectivityManager conMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null)
            return false;
        if (!i.isConnected())
            return false;
        if (!i.isAvailable())
            return false;
        return true;
    }
    
    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    
     public static class KeyValue {
        String key;
        String value;

        public KeyValue(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            if (isNetworkConnected()) {
                try {
                    URL url = new URL(LINK);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    byte[] outputBytes;

                    urlConnection.setRequestMethod(METHOD);
                    urlConnection.connect();


                    switch (METHOD) {
                        case METHOD_GET:
                            break;
                        case METHOD_DELETE:
                            break;
                        case METHOD_PUT:
                            break;
                        case METHOD_POST:
                            outputBytes = postParams.getBytes("UTF-8");
                            OutputStream os = urlConnection.getOutputStream();
                            os.write(outputBytes);
                            os.close();
                            break;
                    }


                    statusCode = urlConnection.getResponseCode();

                    if (statusCode == HttpsURLConnection.HTTP_OK) {
                        InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                        ResponseData = convertStreamToString(inputStream);
                    } else {
                        ResponseData = "";
                        delegate.onFailure(statusCode, "Failed.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    delegate.onFailure(statusCode, e.getMessage());
                }
                if (ResponseData != null && ResponseData.length() > 0)
                    delegate.onSuccess(ResponseData);
            } else {
                delegate.onFailure(500, "NoInternet");
            }

            return ResponseData;
        }


        @Override
        protected void onPostExecute(String s) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            super.onPostExecute(s);
        }

    }
