package com.example.karmarkarsourabh.surat_job_expo.Adaptor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karmarkarsourabh.surat_job_expo.R;
import com.example.karmarkarsourabh.surat_job_expo.Utill.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Archana on 10/25/2018.
 */

public class StudentCompanySelectionAdapter extends RecyclerView.Adapter<StudentCompanySelectionAdapter.MyViewHolder> {
    Context context;
    int success=1;
    ArrayList<HashMap> companyList;
    int[] selectedCompanyIds;
    public static int selectCount=0;
    private String TAG_JOB_FAIR_ID="job_fair_id";
    private String TAG_COMPANY_ID="company_id";
    private String TAG_COMPANY_NAME="company_name";
    private String TAG_COMPANY_LOCATION="company_address";

    /*All for loading Course list */
    String[] postNames;
    int[] postIds;
    int[] selectedPost;
    String selctedPostString="";
    private String BASE_URL,URL;
    OkHttpClient client;
    Session session;
    String NoPostMSG="";
    //Json
    private String TAG_SUCCESS="success";
    private String TAG_MESSAGE="message";
    private String ARRAY_NAME="company_job_post";
    private String TAG_COMPANY_JOB_POST_ID="Company_job_post_id";
    private String TAG_COMPANY_POST_NAME="post_name";


    public StudentCompanySelectionAdapter(Context context, ArrayList<HashMap> companyListList) {
        this.context = context;
        this.companyList = companyListList;
        //student can select maximum 3 companies only
        selectedCompanyIds=new int[3];
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v=LayoutInflater.from(context).inflate(R.layout.student_company_selection_cardview_layout,null);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
            HashMap<String,String> obj=companyList.get(position);

        if(obj.containsKey(TAG_MESSAGE)) {
            holder.companyName.setText(obj.get(TAG_MESSAGE));
            holder.companyLocation.setVisibility(View.INVISIBLE);
            holder.imageButton.setVisibility(View.INVISIBLE);
        }
        else {
            holder.job_fair_id= Integer.parseInt(obj.get(TAG_JOB_FAIR_ID));
            holder.companyId = Integer.parseInt(obj.get(TAG_COMPANY_ID));
            holder.companyName.setText(obj.get(TAG_COMPANY_NAME));
            holder.companyLocation.setText(obj.get(TAG_COMPANY_LOCATION));
            //initially all background icon are 1
            holder.background = 1;
            holder.imageButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
//                    if (holder.background == 0) {
//                        if(selectCount>0) {
//
//                            holder.imageButton.setBackground(context.getDrawable(R.drawable.ic_plus_add_company));
//                            selectCount--;
//                            selectedCompanyIds[selectCount] = 0;
//                            Log.d("selectedCompanyId", selectedCompanyIds[selectCount] + "");
//
//                            holder.background = 1;
//
//
//                        }
//                    } else

                        if (holder.background == 1) {
                        if (selectCount <= 2) {

/*
*   When student click on plus icon of company display him with list of post relevant to his
*   course in that company ,if he applies then perform insert operation
* */
                        new AsycDataloadforJobList(holder.job_fair_id,holder.companyId,holder).execute();

                        } else {
                            Toast.makeText(context, "You can select maimum 3 companies", Toast.LENGTH_SHORT).show();

                        }
                    }

                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return companyList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        int job_fair_id;
        int companyId;
        //For checking background
        //1 mean plus icon 0 mean check icon
        int background;
        TextView companyName,companyLocation;
        ImageButton imageButton;
        public MyViewHolder(View itemView) {
            super(itemView);
            companyName=itemView.findViewById(R.id.textview_studentCompanySelectionCardview_companyName);
            companyLocation=itemView.findViewById(R.id.textview_studentCompanySelectionCardview_companyLocation);
            imageButton=itemView.findViewById(R.id.imageButton_studentCompanySelectionCardview_AddCompany);
        }
    }

    class AsycDataloadforJobList extends AsyncTask<String,String,String>
    {
        ProgressDialog pd;
        int job_fair_id,company_id;
        MyViewHolder holder;
        public AsycDataloadforJobList(int job_fair_id, final int company_id, final MyViewHolder holder)
        {
            this.job_fair_id=job_fair_id;
            this.company_id=company_id;
            this.holder=holder;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            BASE_URL=context.getResources().getString(R.string.BASE_URL);
            session=new Session(context);
            client=new OkHttpClient();
            URL=BASE_URL+"student_display_job_post_relevant_to_course_in_company.php";
            pd=new ProgressDialog(context);
            pd.setMessage("Please wait data is being loaded..");
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected String doInBackground(final String... strings) {
            RequestBody formbody = new FormBody.Builder()
                    .add("job_fair_id",job_fair_id+"")
                    .add("job_seeker_id",session.getLoginID()+"")
                    .add("company_id",company_id+"")
                    .build();


            final Request req = new Request.Builder()
                    .url(URL)
                    .post(formbody)
                    .build();


            client.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("You got error",e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String rs = response.body().string();
                        Log.d("ResForpostInCompany",rs);
                        JSONObject jsonObject = new JSONObject(rs);
                        success = jsonObject.getInt(TAG_SUCCESS);

                        if(success == 1)
                        {
                            JSONArray jsonArray = jsonObject.getJSONArray(ARRAY_NAME);
                            postIds=new int[jsonArray.length()];
                            postNames=new String[jsonArray.length()];
                            selectedPost=new int[jsonArray.length()];
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                postIds[i]=jsonObject1.getInt(TAG_COMPANY_JOB_POST_ID);
                                postNames[i]=jsonObject1.getString(TAG_COMPANY_POST_NAME);

                                Log.d("ResCompPostList",jsonObject1.toString());

                            }


                        }
                        else if(success==2)
                        {
                            NoPostMSG=jsonObject.getString(TAG_MESSAGE);

                        }
                        //Pass Logged-In student id also which can be get through session
                        pd.dismiss();
                      final Handler mHandler = new Handler(Looper.getMainLooper());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        String btnCancel="Cancel";
                                        AlertDialog.Builder ab=new AlertDialog.Builder(context);
                                        ab.setTitle("Post relevant to You in The company");
                                        if(success==1) {
                                            ab.setMultiChoiceItems(postNames, null, new DialogInterface.OnMultiChoiceClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                                                    if(isChecked)
                                                    {
                                                        selectedPost[which]=postIds[which];
                                                    }
                                                    else
                                                    {
                                                        selectedPost[which]=0;
                                                    }
                                                    Log.d("selction ",""+selectedPost[which]);
                                                }
                                            });

                                            ab.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    int flag=0;
                                                    selctedPostString="";

                                                    for(int i=0;i<selectedPost.length;i++)
                                                    {
                                                        if(selectedPost[i]!=0)
                                                        {
                                                            flag=1;
                                                            selctedPostString=selctedPostString+selectedPost[i]+" ";
                                                        }
                                                    }

                                                      if(flag==0)
                                                      {
                                                          //If student do not select any post then show toast
                                                          Toast.makeText(context,"You need to apply for at lease one post",Toast.LENGTH_SHORT).show();
                                                      }
                                                      else {
                                                                //Perform Insertion

                                                          Log.d("selctedPostString",selctedPostString);
                                                          OkHttpClient clientInsert=new OkHttpClient();
                                                          URL=BASE_URL+"student_insert_priority_tb.php";
                                                          RequestBody formbody = new FormBody.Builder()
                                                                  .add("job_seeker_id",session.getLoginID())
                                                                  .add("job_fair_id",holder.job_fair_id+"")
                                                                  .add("selctedPostString",selctedPostString)
                                                                  .add("company_id",holder.companyId+"")
                                                                  .build();
                                                          final Request req = new Request.Builder()
                                                                  .url(URL)
                                                                  .post(formbody)
                                                                  .build();

                                                          clientInsert.newCall(req).enqueue(new Callback() {
                                                              @Override
                                                              public void onFailure(Call call, IOException e)
                                                              {
                                                                  Log.d("Msg",e.getMessage());
                                                              }

                                                              @Override
                                                              public void onResponse(Call call, Response response) throws IOException {
                                                                  try {
                                                                      String rs = response.body().string();
                                                                      Log.d("response",rs);
                                                                      JSONObject jobj = new JSONObject(rs);
                                                                      int success = jobj.getInt(TAG_SUCCESS);
                                                                      final String msg = jobj.getString(TAG_MESSAGE);
                                                                      Log.d("Status code",success+"");
                                                                      Log.d("Status msg",msg);
                                                                      if(success==1)
                                                                      {
                                                                          final Handler mHandler2 = new Handler(Looper.getMainLooper());
                                                                          new Thread(new Runnable() {
                                                                              @Override
                                                                              public void run() {

                                                                                  mHandler2.post(new Runnable() {
                                                                                      @Override
                                                                                      public void run() {
                                                                                          Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                                                                                          //on the success of insertion
                                                                                          holder.imageButton.setBackground(context.getDrawable(R.drawable.ic_check_24dp));
                                                                                          //now background has became check arrow so
                                                                                          holder.background = 0;
                                                                                          selectedCompanyIds[selectCount] = holder.companyId;
                                                                                          Log.d("selectedCompanyId", selectedCompanyIds[selectCount] + "");
                                                                                          selectCount++;
                                                                                      }
                                                                                  });
                                                                              }
                                                                          }).start();

                                                                      }

                                                                  }catch (JSONException e){
                                                                      e.printStackTrace();
                                                                  }
                                                              }
                                                          });




                                                      }
                                                }
                                            });
                                        }
                                        else
                                        {
                                            ab.setMessage(NoPostMSG);
                                            btnCancel="Ok";
                                        }
                                        ab.setNegativeButton(btnCancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                        ab.show();


                                    }
                                });



                            }
                        }).start();

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }

                }
            });

            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

}
