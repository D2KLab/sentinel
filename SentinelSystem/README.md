# IOA system currently implementing
We are implementing IOA system right now, all the codes we did are in the IOA folder.

## Approach


## How to run
Run the Main.java class with following parameters

### train a system
Firstly extract the features of trainDataset(2nd param).txt which is in /resources/tweets/, then  the user have an option to save the features in a file as the name: Trained-Features-IOA + saveFeaturesFile(3rd param).arff, by default system saves the features as "Trained-Features-IOA.arff". The features file saves in /resources/arff/. Attention please don not add the ".txt" suffix of the trainDataset, saveFeatureFile is optional.
<br />params: train trainDataset [saveFeaturesFile]

### evaluation the system
Firstly train a system with the featuresFile(param3).arff which locates in /resources/arff/, by defaul train with the Trained-Features-IOA.arff file. Evaluate the system with testDataset(param2).txt which is a fle in /resources/tweets/. Attention please donnot add the ".txt" suffix of the dataset, do not add the ".arff" suffix of the featuresFile, either. And featureFile is optional.
<br />params: eval testDataset [featuresFile] 

### output
System will output the result(including good prediction and bad prediction) to /output/result.txt, output the errors to /output/error\_analysis/error.txt

### Examples
    "I drove a Linconl and it's a truly dream" 
    Linconl -> proper noun -> positive

    "I drove a Linconl and it was awful"
    Linconl -> proper noun -> negative
    
