package com.example.karmarkarsourabh.surat_job_expo.Adaptor;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.karmarkarsourabh.surat_job_expo.GETSET.Course;
import com.example.karmarkarsourabh.surat_job_expo.R;
import com.example.karmarkarsourabh.surat_job_expo.Utill.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lenovo on 10-18-2018.
 */

public class College_Course_Manage_Adapter extends RecyclerView.Adapter<College_Course_Manage_Adapter.ViewHolder> {

   String BASE_URL,COURSE_DELETE_BY_ID_URL,COURSE_EDIT_URL;
    OkHttpClient client;
private List<Course> courselist;
private Context context;
    Course clsit;
    EditText e;
   private Session session;



public  College_Course_Manage_Adapter(List<Course> lst, Context context)
{
    this.courselist=lst;
    this.context=context;

}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


    session=new Session(context);
        BASE_URL=context.getResources().getString(R.string.BASE_URL);

        COURSE_DELETE_BY_ID_URL=BASE_URL+"course_delete_by_id.php";
        COURSE_EDIT_URL=BASE_URL+"course_edit.php";

       View v= LayoutInflater.from(context).inflate(R.layout.college_course_manage_cardview_layout,null);
        return new ViewHolder(v);


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position) {


        clsit=courselist.get(position);
        holder.txtvwcoursename.setText(clsit.getCourse_name());

      holder.buttondelete.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              AlertDialog.Builder alert=new AlertDialog.Builder(context);
              alert.setTitle("delete Course Name");

              alert.setMessage("are you sure you want to delete?");

              alert.setPositiveButton("delete", new DialogInterface.OnClickListener() {

                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                      client=new OkHttpClient();

                      RequestBody formBody = new FormBody.Builder()
                              .add("id",session.getLoginID())
                              .add("Course_id", String.valueOf(clsit.getCourse_id()))
                              .build();

                      Request req = new Request.Builder()
                              .url(COURSE_DELETE_BY_ID_URL)
                              .post(formBody)
                              .build();


                      client.newCall(req).enqueue(new Callback() {
                          @Override
                          public void onFailure(Call call, IOException e) {
                              Log.d("Error", e.getMessage());
                          }

                          @Override
                          public void onResponse(Call call, final Response response) throws IOException {


                              try {
                                  String rs = response.body().string();

                                  JSONObject jobj = new JSONObject(rs);
                                  int success = jobj.getInt("SUCCESS");

                                  if (success == 1) {

                                      notifyItemRemoved(position);
                                      courselist.remove(position);

                                  }
                              } catch (IOException e) {
                                  e.printStackTrace();
                              } catch (JSONException e) {
                                  e.printStackTrace();
                              }
                          }
                      });
                  }
              });

              AlertDialog a=alert.create();
              a.show();






          }
      });

          }




    @Override
    public int getItemCount() {
        return courselist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {


        public TextView txtvwcoursename;
        public Button buttondelete;
        //public Button buttonedit;

        public ViewHolder(View itemView) {

            super(itemView);

                  txtvwcoursename=(TextView)itemView.findViewById(R.id.textview_college_course_manage_cardview_layout_coursename);
                    buttondelete=itemView.findViewById(R.id.button_college_course_manage_cardview_layout_delete);
                   // buttonedit=itemView.findViewById(R.id.button_college_course_manage_cardview_layout_edit);

        }


    }
}
