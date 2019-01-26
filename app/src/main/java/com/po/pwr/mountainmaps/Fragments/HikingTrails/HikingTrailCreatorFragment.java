package com.po.pwr.mountainmaps.Fragments.HikingTrails;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JsonNode;
import com.po.pwr.mountainmaps.Activities.MainActivity;
import com.po.pwr.mountainmaps.Models.HikingTrailViewModel;
import com.po.pwr.mountainmaps.Models.PointViewModel;
import com.po.pwr.mountainmaps.R;
import com.po.pwr.mountainmaps.Utils.Adapters.PointListAdapter;
import com.po.pwr.mountainmaps.Utils.Tasks.SpringRequestTask;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;

import static android.content.Intent.EXTRA_TITLE;
import static com.po.pwr.mountainmaps.Activities.MainActivity.hiker_id;
import static com.po.pwr.mountainmaps.Activities.MainActivity.request_address;

public class HikingTrailCreatorFragment extends Fragment {


    public final static Integer id = 3;
    public String title;

    private HikingTrailViewModel hikingTrail;
    private final List<PointViewModel> currentTrailPoints = new ArrayList<>();

    public HikingTrailCreatorFragment() {
    }

    public static HikingTrailCreatorFragment newInstance(String title, HikingTrailViewModel hikingTrail) {
        HikingTrailCreatorFragment fragment = new HikingTrailCreatorFragment();
        Bundle bundle = new Bundle(2);
        bundle.putString(EXTRA_TITLE, title);
        bundle.putSerializable("trail", hikingTrail);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static HikingTrailCreatorFragment newInstance(String title) {
        HikingTrailCreatorFragment fragment = new HikingTrailCreatorFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString(EXTRA_TITLE, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.hiking_trail_creator_fragment, container, false);

        title = getArguments().getString(EXTRA_TITLE);
        hikingTrail = (HikingTrailViewModel) getArguments().getSerializable("trail");

        updateData(view);
        updatePointList(view, currentTrailPoints);

        setUpReverseButton(view);
        setUpInfoButton(view);
        setUpAddButton(view);
        setUpSaveButton(view);

        return view;
    }

    public void setUpReverseButton(final View view) {
        Button reverseButton = view.findViewById(R.id.reverseButton);
        reverseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentTrailPoints.size() > 1) {
                    Collections.reverse(currentTrailPoints);
                    updatePointList(getView(), currentTrailPoints);
                } else {
                    Toast.makeText(getContext(), "Podania trasa nie ma zdefiniowanych żadnych punktów!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setUpInfoButton(final View view) {
        Button infoButton = view.findViewById(R.id.infoButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentTrailPoints.size() > 1) {
                    String infoRequest = request_address + "/points/details?points=";

                    for (int i = 0; i < currentTrailPoints.size(); i++) {
                        infoRequest += currentTrailPoints.get(i).getId();

                        if (i < currentTrailPoints.size() - 1)
                            infoRequest += ",";
                    }

                    Log.d("info_request", infoRequest);

                    new SpringRequestTask<JsonNode>(HttpMethod.GET, new SpringRequestTask.OnSpringTaskListener<JsonNode>() {
                        @Override
                        public ResponseEntity<JsonNode> request(RestTemplate restTemplate, String url, HttpMethod method) {
                            return restTemplate.exchange(url, method, null, JsonNode.class);
                        }

                        @Override
                        public void onTaskExecuted(ResponseEntity<JsonNode> result) {
                            JsonNode response = result.getBody();
                            String info = prepareDialogData(response);
                            showInfoDialog(info);
                        }
                    }).execute(infoRequest);
                } else {
                    Toast.makeText(getContext(), "Podana trasa nie jest prawidłowa", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setUpAddButton(final View view) {
        Button addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSearchDialog();
            }
        });

        addButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                if(currentTrailPoints.size() != 2){
                    Toast.makeText(getContext(), "Trasa może zostać automatycznie wygenerowana tylko przy 2 określonych punktach!", Toast.LENGTH_LONG).show();
                } else {
                    createGenerateDialog();
                }
                return true;
            }
        });
    }

    public void setUpSaveButton(final View view) {
        final EditText trailName = view.findViewById(R.id.hikingTrailName);
        final EditText trailDate = view.findViewById(R.id.hikingTrailDate);

        Button saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String requestString = request_address;
                if(hikingTrail == null) { /*dodanie nowej trasy*/

                    requestString += "hikers/" + hiker_id
                            + "/hiking_trails/add?trail_name=" + trailName.getText().toString().replace(" ", "%20")
                            + "&trail_date=" + parseDate(trailDate.getText().toString())
                            + "&trail_points=";

                    for (int i = 0; i < currentTrailPoints.size(); i++) {
                        requestString += currentTrailPoints.get(i).getId();

                        if(i < currentTrailPoints.size() - 1)
                            requestString += ",";
                    }

                    Log.d("punkty_c", requestString);
                } else { /*modyfikacja istniejacej trasy*/
                    requestString += "hiking_trails/" + hikingTrail.getId()
                            + "/update?name=" + trailName.getText().toString().replace(" ", "%20")
                            + "&date=" + trailDate.getText().toString()
                            + "&pointList=";

                    for (int i = 0; i < currentTrailPoints.size(); i++) {
                        requestString += currentTrailPoints.get(i).getId();

                        if(i < currentTrailPoints.size() - 1)
                            requestString += ",";
                    }

                    requestString += "&finished=" + hikingTrail.isFinished();

                    Log.d("punkty_m", requestString);
                }

                new SpringRequestTask<>(HttpMethod.POST, new SpringRequestTask.OnSpringTaskListener<String>() {
                    @Override
                    public ResponseEntity<String> request(RestTemplate restTemplate, String url, HttpMethod method) {
                        return restTemplate.exchange(url, method, null, String.class);
                    }

                    @Override
                    public void onTaskExecuted(ResponseEntity<String> result) {
                        String response = result.getBody();
                        String toastText = "";
                        boolean trailCreated = false;
                        if(response.equals("err_not_enough_points")) {
                            toastText = "Niewystarczająca ilość punktów";
                        } else if(response.equals("err_begin_equals_end")) {
                            toastText = "Punkt startowy i końcowy odcinka jest taki sam!";
                        } else if(response.equals("err_no_points_found")) {
                            toastText = "Błąd przy tworzeniu odcinka";
                        } else if(response.equals("err_section_fail")) {
                            toastText = "Błąd przy tworzeniu odcinka!";
                        } else {
                            toastText = "Pomyślnie utworzono trasę!";
                            trailCreated = true;
                        }

                        Toast.makeText(getContext(), toastText, Toast.LENGTH_SHORT).show();

                        if(trailCreated)
                            getFragmentManager().popBackStack();
                    }

                }).execute(requestString);
            }
        });
    }

    public Date parseDate(String dateString) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = null;

        try {
            date = sdf1.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        return sqlDate;
    }

    public boolean getGeneratedPointList(final View view, Integer min_length, Integer max_length) {
        boolean result = true;
        List<PointViewModel> generatedPointList = new ArrayList<>();

        Integer begin_id = currentTrailPoints.get(0).getId();
        Integer end_id = currentTrailPoints.get(1).getId();

                    /*
                       DIALOG z max min odleglosc ;)))))
                     */
        new SpringRequestTask<>(HttpMethod.GET, new SpringRequestTask.OnSpringTaskListener<List<PointViewModel>>() {
            @Override
            public ResponseEntity<List<PointViewModel>> request(RestTemplate restTemplate, String url, HttpMethod method) {
                return restTemplate.exchange(url, method, null, new ParameterizedTypeReference<List<PointViewModel>>() {
                });
            }

            @Override
            public void onTaskExecuted(ResponseEntity<List<PointViewModel>> result) {
                List<PointViewModel> response = result.getBody();
                if(!response.isEmpty()) {
                    currentTrailPoints.clear();
                    currentTrailPoints.addAll(response);
                    updatePointList(view, currentTrailPoints);
                }
            }
        }).execute(request_address + "/hiking_trails/generate?begin_id=" + begin_id + "&end_id=" + end_id + "&min=" + min_length + "&max=" + max_length);

        if(currentTrailPoints.isEmpty()) {
            Toast.makeText(getContext(), "Nie można stworzyć trasy z podanych odcinków!", Toast.LENGTH_SHORT).show();
            result = false;
        }
        return result;
    }

    public void createGenerateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.hiking_trail_creator_generate_dialog, null);
        builder.setView(view)
                .setPositiveButton(R.string.track_list_dialog_agree, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText minLengthField = view.findViewById(R.id.minLengthField);
                        EditText maxLengthField = view.findViewById(R.id.maxLengthField);

                        Integer min_length = Integer.parseInt(minLengthField.getText().toString());
                        Integer max_length = Integer.parseInt(maxLengthField.getText().toString());
                        if(min_length > 0 && max_length > 0) {
                            if(max_length > min_length) {
                                getGeneratedPointList(getView(), min_length, max_length);
                            } else {
                                Toast.makeText(getContext(), "Maksymalna długość trasy musi być większa od długości minimalnej!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Podane wartości muszą być większe od 0!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(R.string.track_list_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog generateDialog = builder.create();
        generateDialog.setTitle("Wprowadź minimalną i maksymalną długość odcinka!");
        generateDialog.show();
    }

    public void createSearchDialog() {
        final PointViewModel result = new PointViewModel();

        new SimpleSearchDialogCompat<>(getContext(), getResources().getString(R.string.search_dialog_title),
                "Podaj nazwę odcinka", null, initSearchData(), new SearchResultListener<Searchable>() {
            @Override
            public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                for(Map.Entry<Integer, PointViewModel> entry : MainActivity.pointSet.entrySet()) {
                    if(entry.getValue().getName().equals(searchable.getTitle())) {
                        Log.d("model " + entry.getValue().getId(), entry.getValue().toString());
                        result.setId(entry.getValue().getId());
                        result.setName(entry.getValue().getName());

                    }
                }

                if(result.getName() != null) {
                    currentTrailPoints.add(result);
                    updatePointList(getView(), currentTrailPoints);
                } else {
                    Toast.makeText(getContext(), "Podany odcinek nie istnieje!", Toast.LENGTH_SHORT).show();
                }
                baseSearchDialogCompat.dismiss();
            }
        }).show();
    }

    public ArrayList<Searchable> initSearchData() {
        ArrayList<Searchable> searchList = new ArrayList<>();

        for(Map.Entry<Integer, PointViewModel> entry : MainActivity.pointSet.entrySet()) {
            if(currentTrailPoints.isEmpty() || !entry.getValue().sameName(currentTrailPoints.get(currentTrailPoints.size() - 1))) {
                searchList.add(entry.getValue());
            }
        }

        return searchList;
    }

    public void updateData(final View view) {
        MainActivity activity = ((MainActivity) getActivity());

        EditText editName = view.findViewById(R.id.hikingTrailName);
        EditText editDate = view.findViewById(R.id.hikingTrailDate);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String formattedDate = dateFormat.format(cal.getTime());
        editDate.setText(formattedDate);

        if(activity != null) {
            activity.curr_fragment = id;
            activity.getSupportActionBar().setTitle(title);
        }

        if(hikingTrail != null) {
            editName.setText(hikingTrail.getName());
            editDate.setText(hikingTrail.getDate().toString());

            HashMap<Integer, PointViewModel> allPoints = ((MainActivity) getActivity()).pointSet;

            for (Integer i : hikingTrail.getPoints())
                currentTrailPoints.add(allPoints.get(i));
        }
    }
    public void updatePointList(View view, List<PointViewModel> newPointList) {
        RecyclerView mRecyclerView;
        RecyclerView.LayoutManager mLayoutManager;

        mRecyclerView = view.findViewById(R.id.hikingTrailPointsList);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        
        PointListAdapter mRecyclerAdapter = new PointListAdapter(newPointList);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    public String prepareDialogData(JsonNode response) {
        EditText editName = getView().findViewById(R.id.hikingTrailName);

        Double distance = response.get("dist").asDouble(0) / 1000;
        Integer points = response.get("points").asInt(0);
        Double time = response.get("time").asDouble(0);

        Integer hours = (int) Math.floor(time);
        Integer minutes = (int) (60 * (time - Math.floor(time)));

        String result = "";
        result += getResources().getString(R.string.details_title, editName.getText().toString()) + "\n";
        result += getResources().getString(R.string.details_dist, distance) + "\n";
        result += getResources().getString(R.string.details_points, points) + "\n";
        result += getResources().getString(R.string.details_time, hours,  minutes);

        return result;
    }

    public void showInfoDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

}