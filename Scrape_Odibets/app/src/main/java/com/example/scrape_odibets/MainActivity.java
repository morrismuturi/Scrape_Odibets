package com.example.scrape_odibets;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.*;
public class MainActivity extends Activity{

    Module1 m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        m=new Module1();
        m.result=(TextView) findViewById(R.id.textView1);
        m.mytable = (TableLayout) findViewById(R.id.mytable);
        m.result.setMovementMethod(new ScrollingMovementMethod());
        m.getBtn = (Button) findViewById(R.id.getBtn);

        m.getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWebsite();
            }
        });
    }

    private void getWebsite() {
        new Thread(new Runnable() {
            @Override

            public void run() {
                final StringBuilder builder = new StringBuilder();
                final ArrayList mm=new ArrayList();
                final ArrayList bb = new ArrayList();
                try {

                    Document doc=Jsoup.connect("https://odibets.com/standings").get();
                    Elements containers = doc.select("div.results");
                    int contSize=containers.size();

                    if (contSize>0) {
                        int b = containers.indexOf(containers.last());
                        int c = containers.indexOf(containers.first());


                        ArrayList arr2 = new ArrayList();
                        for (int j = c; j <= b; j++) {

                            ArrayList arr1 = new ArrayList();
                            arr1.clear();

                            for (int m = 0; m < containers.get(j).select("tr.results-body").size(); m++) {

                                ArrayList arr = new ArrayList();
                                arr.clear();

                                arr.add(Integer.parseInt(containers.get(j).select("tr.results-body").get(m).select("span").first().text()));
                                arr.add(Integer.parseInt(containers.get(j).select("tr.results-body").get(m).select("span").last().text()));
                                arr1.add(arr);

                            }
                            arr2.add(arr1);

                        }


                        //converting 3d ArrayList to 1d ArrayList

                        for (int i = 0; i < arr2.size(); i++) {
                            for (int j = 0; j < ((ArrayList) arr2.get(i)).size(); j++) {
                                bb.add(((ArrayList) arr2.get(i)).get(j));

                            }
                        }


                        //converting 1d ArrayList to 3D ArrayList;transpose of first 3d ArrayList


                        int size = bb.size();

                        //ArrayList mm = new ArrayList();

                        for (int m = 0; m < bb.size(); m++) {

                            ArrayList mm2 = new ArrayList();

                            mm.add(mm2);
                            mm2.clear();

                            for (int n = m; n < bb.size(); n = n + 8) {
                                //mm2.clear();
                                if (mm.size() <= 8) {
                                    mm2.add(bb.get(n));
                                }


                            }


                        }
                        //removing empty values in the ArrayList
                        mm.removeAll(Collections.singleton(new ArrayList()));


                        for (int z = 0; z < mm.size(); z++) {
                            for (int y = 0; y < ((ArrayList) mm.get(z)).size(); y++) {

                                int j = (Integer) (((ArrayList) ((ArrayList) mm.get(z)).get(y)).get(0)).hashCode();

                                int j1 = (Integer) (((ArrayList) ((ArrayList) mm.get(z)).get(y)).get(1)).hashCode();

                                if (!(j == j1)) {
                                    m.totalNonDraws++;
                                    //break;
                                }

                                if (j == j1) {
                                    m.totalDraws++;
                                    //break;
                                }

                            }
                            for (int y = 0; y < ((ArrayList) mm.get(z)).size(); y++) {

                                int j = (Integer) (((ArrayList) ((ArrayList) mm.get(z)).get(y)).get(0)).hashCode();

                                int j1 = (Integer) (((ArrayList) ((ArrayList) mm.get(z)).get(y)).get(1)).hashCode();
                                if (j == j1) {

                                    break;
                                }

                                m.totalLastFrequentNonDraws++;

                            }


                            ArrayList l = new ArrayList();
                            ArrayList l1 = new ArrayList();
                            ArrayList l2 = new ArrayList();


                            l.add(new Integer(m.totalLastFrequentNonDraws));
                            l1.add(new Integer(m.totalDraws));
                            l2.add(new Integer(m.totalNonDraws));
                            ((ArrayList) mm.get(z)).clear();


                            ((ArrayList) mm.get(z)).add(l);
                            ((ArrayList) mm.get(z)).add(l1);
                            ((ArrayList) mm.get(z)).add(l2);
                            m.totalLastFrequentNonDraws = 0;
                            m.totalNonDraws = 0;
                            m.totalDraws = 0;


                        }
                    }

                    else{
                        String msg="Results for round one not show,Wait for the next ones..";
                        builder.append(msg);
                    }


                }
                catch(Exception e){
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        m.mytable.removeAllViews();

                        /*final TableRow  header=new TableRow(MainActivity.this);
                        header.setBackgroundColor(Color.GREEN);

                        String []s={"Row","#FNondraws","#Draws","#Nondraws"};
                        for(int i=0;i<4;i++) {
                            final TextView cell = new TextView(MainActivity.this);
                            cell.setText(s[i]);
                            header.addView(cell);
                        }

                        //mytable.addView(header);
                        */

                        for(int a=0;a<mm.size();a++){
                            final TableRow  row=new TableRow(MainActivity.this);
                            final TextView rowNumber=new TextView(MainActivity.this);

                            rowNumber.setText(""+(a+1));
                            rowNumber.setTextSize(18);
                            row.addView(rowNumber);
                            for(int a1=0;a1<((ArrayList)mm.get(a)).size();a1++){
                                final TextView text=new TextView(MainActivity.this);
                                text.setTextSize(18);
                                //Toast.makeText(getApplicationContext(),builder.toString(),Toast.LENGTH_LONG).show();
                                String rowValue= (String)(((ArrayList) ((ArrayList) mm.get(a)).get(a1)).get(0)).toString();
                                //int j = (Integer) (((ArrayList) ((ArrayList) mm.get(a)).get(a1)).get(0)).hashCode();
                                //String rowValue=String.valueOf(j);


                                text.setText(rowValue);
                                row.addView(text);


                            }



                            m.mytable.addView(row);


                        }

                        if(bb.size()>0) {
                            final TableRow header = new TableRow(MainActivity.this);
                            String[] s = {"Row", "#FNondraws", "#Draws", "#Nondraws"};
                            for (int i = 0; i < 4; i++) {
                                final TextView cell = new TextView(MainActivity.this);
                                cell.setTextSize(15);
                                cell.setText(s[i]);
                                cell.setPadding(8,0,0,0);
                                header.addView(cell);
                            }

                            TableRow footer = new TableRow(MainActivity.this);
                            TableRow.LayoutParams params = new TableRow.LayoutParams();
                            params.span = 3;

                            TextView cell = new TextView(MainActivity.this);
                            TextView cell1 = new TextView(MainActivity.this);
                            int currentRound = (((bb.size()) / 8) + 1);
                            int remainingrounds=30-currentRound;


                            cell.setLayoutParams(params);
                            cell.setTextSize(15);
                            cell1.setTextSize(15);
                            cell.setText("" + currentRound + "/" + 30 + " Rounds played");
                            cell1.setText(""+remainingrounds+" rounds remaining");
                            footer.addView(cell);
                            footer.addView(cell1);
                            m.mytable.addView(header, 0);
                            m.mytable.addView(footer, 9);
                            TableRow end = new TableRow(MainActivity.this);
                            m.mytable.addView(end);
                            //mytable.addView(header,0);
                            m.getBtn.setText("Review Rows");
                        }








                        if(builder.length()>0){
                            Toast.makeText(getApplicationContext(),builder.toString(),Toast.LENGTH_LONG).show();
                            //m.result.setText(builder);

                        }

                    }
                });
            }
        }).start();
    }
}