 HashMap data = new HashMap();
        data.put("key1", "value1");
        data.put("key2", "value2");
        new AsyncPostData("http://yourPostUrl.com", new AsyncPostData.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                //output is your response 
            }
        }).execute(data);
