package com.po.pwr.mountainmaps.Fragments.HikingTrails;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JsonNode;
import com.po.pwr.mountainmaps.Activities.MainActivity;
import com.po.pwr.mountainmaps.Models.HikingTrailModel;
import com.po.pwr.mountainmaps.Models.PointModel;
import com.po.pwr.mountainmaps.R;
import com.po.pwr.mountainmaps.Utils.Helpers.AdapterUtils;
import com.po.pwr.mountainmaps.Utils.Helpers.ParseHelperUtility;
import com.po.pwr.mountainmaps.Utils.Tasks.SpringRequestTask;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;

import static android.content.Intent.EXTRA_TITLE;
import static com.po.pwr.mountainmaps.Activities.MainActivity.hiker_id;
import static com.po.pwr.mountainmaps.Activities.MainActivity.request_address;

/**
 * Fragment używany do utworzenia nowej lub modyfikacji istniejącej klasy.
 */
public class HikingTrailCreatorFragment extends Fragment {
    private final static Integer id = 3;
    private String title;

    private HikingTrailModel hikingTrail;
    private final List<PointModel> currentTrailPoints = new ArrayList<>();

    public HikingTrailCreatorFragment() {
        //Create new HikingTrailCreatorFragment
    }

    /** Konstruktor umożliwający stworzenie fragmentu do modyfikacji istniejącej klasy
     * @param title Tytuł fragmentu
     * @param hikingTrail   Trasa do modyfikacji
     * @return  Utworzony fragment
     */
    public static HikingTrailCreatorFragment newInstance(String title, HikingTrailModel hikingTrail) {
        HikingTrailCreatorFragment fragment = new HikingTrailCreatorFragment();
        Bundle bundle = new Bundle(2);
        bundle.putString(EXTRA_TITLE, title);
        bundle.putSerializable("trail", hikingTrail);
        fragment.setArguments(bundle);
        return fragment;
    }

    /** Konstruktor umożliwjaący stworzenie fragmentu do utworzenia nowej trasy
     * @param title Tytuł fragmentu
     * @return Utworzony fragment
     */
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

        title = Objects.requireNonNull(getArguments()).getString(EXTRA_TITLE);
        hikingTrail = (HikingTrailModel) getArguments().getSerializable("trail");

        updateData(view);
        AdapterUtils.updateRecyclerViewPointList(view, getContext(), currentTrailPoints);

        setUpReverseButton(view);
        setUpInfoButton(view);
        setUpAddButton(view);
        setUpSaveButton(view);

        return view;
    }

    /** Metoda definiujaca zachowanie dla przycisku Odwróć
     * @param view Obecny widok
     */
    private void setUpReverseButton(final View view) {
        Button reverseButton = view.findViewById(R.id.reverseButton);
        reverseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentTrailPoints.size() > 1) {
                    Collections.reverse(currentTrailPoints);
                    AdapterUtils.updateRecyclerViewPointList(Objects.requireNonNull(getView()), getContext(), currentTrailPoints);
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.err_no_points_to_reverse), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /** Metoda definiujaca zachowanie dla przycisku Info
     * @param view Obecny widok
     */
    private void setUpInfoButton(final View view) {
        Button infoButton = view.findViewById(R.id.infoButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentTrailPoints.size() > 1) {
                    String infoRequest = request_address + "/points/details?points=";

                    for (int i = 0; i < currentTrailPoints.size(); i++) {
                        infoRequest += currentTrailPoints.get(i).getId();

                        if (i < currentTrailPoints.size() - 1) {
                            infoRequest += ",";
                        }
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
                            String info = ParseHelperUtility.prepareDialogData(Objects.requireNonNull(getContext()), Objects.requireNonNull(getView()), response);
                            showInfoDialog(info);
                        }
                    }).execute(infoRequest);
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.err_not_enough_points), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /** Metoda definiujaca zachowanie dla przycisku Dodaj
     * @param view Obecny widok
     */
    private void setUpAddButton(final View view) {
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

    /** Metoda definiujaca zachowanie dla przycisku Zapisz
     * @param view Obecny widok
     */
    private void setUpSaveButton(final View view) {
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
                            + "&trail_date=" + ParseHelperUtility.parseDate(trailDate.getText().toString())
                            + "&trail_points=";

                    for (int i = 0; i < currentTrailPoints.size(); i++) {
                        requestString += currentTrailPoints.get(i).getId();

                        if(i < currentTrailPoints.size() - 1) {
                            requestString += ",";
                        }
                    }

                    Log.d("punkty_c", requestString);
                } else { /*modyfikacja istniejacej trasy*/
                    requestString += "hiking_trails/" + hikingTrail.getId()
                            + "/update?name=" + trailName.getText().toString().replace(" ", "%20")
                            + "&date=" + trailDate.getText().toString()
                            + "&pointList=";

                    for (int i = 0; i < currentTrailPoints.size(); i++) {
                        requestString += currentTrailPoints.get(i).getId();

                        if(i < currentTrailPoints.size() - 1) {
                            requestString += ",";
                        }
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
                        String toastText;
                        boolean trailCreated = false;

                        switch (response) {
                            case "err_not_enough_points":
                                toastText = getResources().getString(R.string.err_not_enough_points);
                                break;
                            case "err_begin_equals_end":
                                toastText = getResources().getString(R.string.err_begin_equals_end);
                                break;
                            case "err_no_points_found":
                                toastText = getResources().getString(R.string.err_no_points_found);
                                break;
                            case "err_section_fail":
                                toastText = getResources().getString(R.string.err_section_fail);
                                break;
                            default:
                                toastText = getResources().getString(R.string.err_no_error);
                                trailCreated = true;
                                break;
                        }

                        Toast.makeText(getContext(), toastText, Toast.LENGTH_SHORT).show();

                        if(trailCreated) {
                            Objects.requireNonNull(getFragmentManager()).popBackStack();
                        }
                    }

                }).execute(requestString);
            }
        });
    }

    /** Metoda pobierajaca i zapisujaca wygenerowana liste punktow z serwera
     * @param view Obecny widok
     * @param min_length Minimalna dlugosc wygenerowanej trasy
     * @param max_length Maksymalna dlugosc wygenerowanej trasy
     * @return Czy wygenerowanie trasy sie powiodlo
     */
    public boolean getGeneratedPointList(final View view, Integer min_length, Integer max_length) {
        boolean result = true;

        if(currentTrailPoints.size() != 2) {
            Toast.makeText(getContext(), "Nie można stworzyć trasy z podanych odcinków!", Toast.LENGTH_SHORT).show();
            result = false;
        } else if(min_length < 1 || max_length < 1) {
            Toast.makeText(getContext(), "Długość trasy musi być większa od zera!", Toast.LENGTH_SHORT).show();
            result = false;
        } else if(min_length > max_length) {
            Toast.makeText(getContext(), "Długość maksymalna nie może być mniejsza od długości minimalnej!", Toast.LENGTH_SHORT);
            result = false;
        } else {
            Integer begin_id = currentTrailPoints.get(0).getId();
            Integer end_id = currentTrailPoints.get(1).getId();

            new SpringRequestTask<>(HttpMethod.GET, new SpringRequestTask.OnSpringTaskListener<List<PointModel>>() {

                @Override
                public ResponseEntity<List<PointModel>> request(RestTemplate restTemplate, String url, HttpMethod method) {
                    return restTemplate.exchange(url, method, null, new ParameterizedTypeReference<List<PointModel>>() {
                    });
                }

                @Override
                public void onTaskExecuted(ResponseEntity<List<PointModel>> result) {
                    List<PointModel> response = result.getBody();
                    if(response != null && !response.isEmpty()) {
                        setUpPointList(response);
                        AdapterUtils.updateRecyclerViewPointList(Objects.requireNonNull(getView()), getContext(), currentTrailPoints);
                    }
                }
            }).execute(request_address + "/hiking_trails/generate?begin_id=" + begin_id + "&end_id=" + end_id + "&min=" + min_length + "&max=" + max_length);

        }
        return result;
    }

    /** Metoda tworzaca Dialog umozliwiajacy wprowadzenie danych do generowania trasy
     *
     */
    private void createGenerateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
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

    /** Metoda uruchamiajaca Dialog z lista odcinkow do dodania
     *
     */
    private void createSearchDialog() {
        final PointModel result = new PointModel();
        new SimpleSearchDialogCompat<>(getContext(), getResources().getString(R.string.search_dialog_title),
                "Podaj nazwę odcinka", null, (ArrayList) initSearchData(currentTrailPoints), new SearchResultListener<Searchable>() {
            @Override
            public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {

                for(Map.Entry<Integer, PointModel> entry : MainActivity.pointSet.entrySet()) {
                    if(entry.getValue().getName().equals(searchable.getTitle())) {
                        Log.d("model " + entry.getValue().getId(), entry.getValue().toString());
                        result.setId(entry.getValue().getId());
                        result.setName(entry.getValue().getName());

                    }
                }

                if(result.getName() != null) {
                    currentTrailPoints.add(result);
                    AdapterUtils.updateRecyclerViewPointList(Objects.requireNonNull(getView()), getContext(), currentTrailPoints);
                } else {
                    Toast.makeText(getContext(), "Podany odcinek nie istnieje!", Toast.LENGTH_SHORT).show();
                }

                baseSearchDialogCompat.dismiss();
            }
        }).show();
    }

    /** Metoda iniciujaca dane dla Dialogu z lista punktow
     * @param trailPoints Lista mozliwych punktow
     * @return Lista mozliwych do dodania punktow
     */
    private List<Searchable> initSearchData(List<PointModel> trailPoints) {
        ArrayList<Searchable> searchList = new ArrayList<>();

        for(Map.Entry<Integer, PointModel> entry : MainActivity.pointSet.entrySet()) {
            if(trailPoints.isEmpty() || !entry.getValue().sameName(trailPoints.get(trailPoints.size() - 1))) {
                searchList.add(entry.getValue());
            }
        }

        return searchList;
    }

    /** Metoda aktualizujaca obecna liste punktow w trasie
     * @param view Obecny widok
     */
    private void updateData(final View view) {
        MainActivity activity = ((MainActivity) getActivity());

        EditText editName = view.findViewById(R.id.hikingTrailName);
        EditText editDate = view.findViewById(R.id.hikingTrailDate);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        Calendar cal = Calendar.getInstance();
        String formattedDate = dateFormat.format(cal.getTime());
        editDate.setText(formattedDate);

        if(activity != null) {
            activity.curr_fragment = id;
            Objects.requireNonNull(activity.getSupportActionBar()).setTitle(title);
        }

        if(hikingTrail != null) {
            editName.setText(hikingTrail.getName());
            editDate.setText(hikingTrail.getDate().toString());

            Map<Integer, PointModel> allPoints = MainActivity.pointSet;

            for (Integer i : hikingTrail.getPoints()) {
                currentTrailPoints.add(allPoints.get(i));
            }
        }
    }

    /** Metoda uruchamiajaca Dialog z informacjami o trasie
     * @param message Ciag znakow do wyswietlenia
     */
    private void showInfoDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /** Metoda pozwalająca na nadpisanie istniejącej listy punktów
     * @param trailPoints Nowa lista punktów
     * @return Nadpisana lista punktów
     */
    public List<PointModel> setUpPointList(List<PointModel> trailPoints) {
        currentTrailPoints.clear();
        currentTrailPoints.addAll(trailPoints);

        return currentTrailPoints;
    }

    /** Metoda zwracająca obecną listę punktów
     * @return Obecna lista punktów
     */
    public List<PointModel> getCurrentTrailPoints() {
        return currentTrailPoints;
    }

}