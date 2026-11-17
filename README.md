# ML Experiment Tracker

This project is a Java-based console application for tracking machine learning experiments.

## Team split
- Developer 1: model hierarchy (`Model`, `DecisionTree`, `NeuralNetwork`)
- Developer 2: experimental data model and tracker (`Experiment`, `ExperimentTracker`)
- Developer 3: console input, validation, and program flow (`Main`)

## Features
- Add experiments
- Store parameters and evaluation metrics
- Search by experiment or model name
- Update or delete experiments
- View all saved experiments
- Validate blank input and invalid numeric values

## Run
```bash
javac *.java
java Main
```

## Design notes
The project uses OOP principles such as encapsulation, inheritance, and polymorphism. Experiments hold model objects and support multiple parameter/result entries via `ArrayList`.
