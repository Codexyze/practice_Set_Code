package com.nutrino.mlmodelinandroid.presenation // Defines the package name for this file

import android.content.Context // Imports Android Context to access app resources like assets
import android.util.Log // Imports the Log class to print messages to the system console (Logcat)
import androidx.compose.foundation.layout.Arrangement // Imports Arrangement to define spacing between UI elements
import androidx.compose.foundation.layout.Column // Imports Column to stack UI elements vertically
import androidx.compose.foundation.layout.fillMaxSize // Imports Modifier to make a layout fill the entire screen
import androidx.compose.foundation.layout.fillMaxWidth // Imports Modifier to make a layout fill the available width
import androidx.compose.foundation.layout.padding // Imports Modifier to add space around UI elements
import androidx.compose.material3.Button // Imports the Material Design 3 Button component
import androidx.compose.material3.OutlinedTextField // Imports the Material Design 3 Text Field with an outline
import androidx.compose.material3.Text // Imports the Material Design 3 Text component to display text
import androidx.compose.runtime.Composable // Imports the annotation that defines a function as a UI component
import androidx.compose.runtime.getValue // Imports delegated property getter for state management
import androidx.compose.runtime.mutableStateOf // Imports function to create a state variable that triggers UI updates
import androidx.compose.runtime.remember // Imports function to keep state values across UI recompositions
import androidx.compose.runtime.setValue // Imports delegated property setter for state management
import androidx.compose.ui.Modifier // Imports the Modifier interface to decorate or augment UI components
import androidx.compose.ui.platform.LocalContext // Imports function to get the current Android Context in a Composable
import androidx.compose.ui.unit.dp // Imports the Density-independent Pixel unit for dimensions
import com.google.ai.edge.litert.Accelerator // Imports LiteRT Accelerator to specify hardware for model execution
import com.google.ai.edge.litert.CompiledModel // Imports LiteRT CompiledModel to load and run ML models


private const val TAG = "ArrayModelScreen" // A constant tag used for identification in Logcat messages

@Composable // Marks this function as a Composable, meaning it defines a piece of the UI
fun ArrayModelScreen() { // The main screen component for the Array Model demonstration

    val context = LocalContext.current // Retrieves the current Android Context for use in the UI and ML functions
    var inputText by remember { mutableStateOf("1, 2, 3, 4") } // State variable to store the user's input string, remembered across refreshes
    var result by remember { // State variable to store the output result of the model or error messages
        mutableStateOf("No Result") // Initializes the result state with a default "No Result" string
    } // Ends the remember block for result

    Column( // A container that arranges its children in a vertical stack
        modifier = Modifier // Starts defining modifiers for the Column
            .fillMaxSize() // Makes the Column take up the full height and width of the screen
            .padding(16.dp), // Adds 16 density-independent pixels of padding around the Column's content
        verticalArrangement = Arrangement.spacedBy(16.dp) // Adds 16dp of space between each child element in the Column
    ) { // Starts the content block of the Column

        OutlinedTextField( // A text input field with a border
            value = inputText, // Binds the text field's content to the inputText state variable
            onValueChange = { inputText = it }, // Updates the inputText state variable whenever the user types
            label = { Text("Input Values (comma separated)") }, // Sets the label text displayed above or inside the field
            modifier = Modifier.fillMaxWidth() // Makes the text field stretch to the full width of the Column
        ) // Ends the OutlinedTextField component

        Button( // A clickable button component
            onClick = { // Defines the action to perform when the button is clicked
                Log.d(TAG, "Run Model clicked") // Logs a debug message indicating the button was pressed
                try { // Starts a try block to handle potential errors during execution
                    // Parse input string to FloatArray
                    val inputData = inputText.split(",") // Splits the input string into a list of strings at each comma
                        .map { it.trim().toFloat() } // Trims whitespace from each string and converts it to a Float
                        .toFloatArray() // Converts the list of Floats into a primitive FloatArray

                    val output = arrayModel(context, inputData) // Calls the ML function and stores the returned FloatArray
                    result = "Output = ${output.joinToString()}" // Formats the output array as a string and updates the result state

                } catch (e: Exception) { // Catches any exception that occurs during parsing or model execution
                    Log.e(TAG, "Error running model", e) // Logs the error details and stack trace to Logcat
                    result = "Error: ${e.message}" // Updates the result state with a user-friendly error message
                } // Ends the catch block
            } // Ends the onClick lambda function
        ) { // Starts the content block for the Button's label
            Text("Run Model") // Displays the text "Run Model" inside the button
        } // Ends the Button component

        Text( // A component to display text on the screen
            text = result // Binds the displayed text to the current value of the result state variable
        ) // Ends the Text component
    } // Ends the Column component
} // Ends the ArrayModelScreen Composable function

private fun arrayModel(context: Context, inputData: FloatArray): FloatArray { // A private function that handles the ML inference logic
    Log.d(TAG, "arrayModel called") // Logs that the ML logic function has started
    val compiledModel = CompiledModel.create( // Creates a new LiteRT model instance
        context.assets, // Uses the app's assets manager to find the model file
        "arraymodel.tflite", // Specifies the filename of the TFLite model in the assets folder
        CompiledModel.Options(Accelerator.CPU) // Configures the model to run on the CPU
    ) // Ends the CompiledModel creation
    Log.d(TAG, "CompiledModel created") // Logs that the model has been successfully loaded into memory

    val inputBuffers = compiledModel.createInputBuffers() // Prepares the memory buffers required for the model's inputs
    val outputBuffers = compiledModel.createOutputBuffers() // Prepares the memory buffers where the model will write its results

    // Write input data
    inputBuffers[0].writeFloat(inputData) // Writes the user's float array into the first input buffer of the model
    Log.d(TAG, "Input data written: ${inputData.joinToString()}") // Logs the exact data that was sent to the model

    compiledModel.run(inputBuffers, outputBuffers) // Executes the machine learning model with the provided inputs
    Log.d(TAG, "Model run completed") // Logs that the inference process finished successfully

    // Read output
    val output = outputBuffers[0].readFloat() // Retrieves the resulting float array from the first output buffer
    Log.d(TAG, "Output read: ${output.joinToString()}") // Logs the raw output received from the model

    return output // Returns the resulting FloatArray back to the caller
} // Ends the arrayModel function
