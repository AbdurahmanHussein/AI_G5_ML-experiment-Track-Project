# ML Experiment Tracker

A Java console application for recording, evaluating, searching, updating, and deleting machine-learning experiments. The project demonstrates object-oriented design, inheritance, polymorphism, collections, and console input validation.

## Features

- Create experiments with a selected model and parameters
- Record multiple evaluation results
- Search experiments by name or model
- Update and delete experiments
- Display all experiments and details
- Validate blank input and invalid numeric values
- Preserve experiments in memory during a run

## Project structure

- `Model.java` — base model abstraction
- `DecisionTree.java` / `NeuralNetwork.java` — model implementations
- `Parameter.java` — experiment parameter data
- `EvaluationResult.java` — model evaluation metrics
- `Experiment.java` — model and parameters for one experiment
- `ExperimentTracker.java` — experiment collection and operations
- `Main.java` — console interface and application workflow

## Requirements

- Java Development Kit (JDK) 8 or newer

## Run locally

From the repository root:

```bash
javac *.java
java Main
```

Enter menu options and follow the prompts to manage experiments.

## Example workflow

1. Start the application.
2. Add a new experiment and choose a model.
3. Enter parameters and evaluation values.
4. Search or update the experiment as needed.
5. Display the complete experiment list or delete a record.

## Object-oriented design

The project applies encapsulation for domain data, inheritance for shared model behavior, polymorphism for model-specific implementations, and `ArrayList` collections for parameters and evaluation results.

## Possible extensions

- Persist experiments to CSV or JSON
- Add test coverage
- Sort and filter by evaluation metrics
- Export comparison reports
- Support additional machine-learning models
