package com.example.unitconverter;
import java.util.ArrayList;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText inputValue;
    EditText outputValue;
    Spinner fromSpinner, toSpinner;
    Button convertButton;
    TextView historyText;
    ArrayList<String> historyList = new ArrayList<>();

    String[] units = {"Metre", "Millimetre", "Centimetre","Mile", "Foot"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize all views after layout is loaded
        inputValue = findViewById(R.id.inputValue);
        outputValue = findViewById(R.id.outputValue);
        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);
        convertButton = findViewById(R.id.convertButton);
        historyText = findViewById(R.id.historyText);

        // Setup unit spinner adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);

        // Handle button click
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = inputValue.getText().toString();
                if (input.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a value", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    double value = Double.parseDouble(input);
                    String fromUnit = fromSpinner.getSelectedItem().toString();
                    String toUnit = toSpinner.getSelectedItem().toString();

                    double result = convertLength(value, fromUnit, toUnit);
                    String resultStr = String.format("%.4f %s", result, toUnit);
                    outputValue.setText(resultStr);

                    // Update history
                    String entry = input + " " + fromUnit + " → " + resultStr;
                    historyList.add(0, entry); // add to top
                    StringBuilder builder = new StringBuilder();
                    String lineSeparator = ("_________________________________________");
                    for (String line : historyList) {
                        builder.append(line).append("\n" + lineSeparator +"\n");
                    }
                    historyText.setText(builder.toString());


                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Invalid number format", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private double convertLength(double value, String fromUnit, String toUnit) {
        // Convert to metres first
        double valueInMetres;
        switch (fromUnit) {
            case "Millimetre":
                valueInMetres = value / 1000.0;
                break;
            case "Mile":
                valueInMetres = value * 1609.34;
                break;
            case "Foot":
                valueInMetres = value * 0.3048;
                break;
            case "Centimetre":
                valueInMetres = value / 100.0;
                break;
            default: // Metre
                valueInMetres = value;
                break;
        }

        // Convert from metres to target unit
        switch (toUnit) {
            case "Millimetre":
                return valueInMetres * 1000.0;
            case "Centimetre":
                return valueInMetres * 100.0;
            case "Mile":
                return valueInMetres / 1609.34;
            case "Foot":
                return valueInMetres / 0.3048;
            default: // Metre
                return valueInMetres;
        }
    }
}
