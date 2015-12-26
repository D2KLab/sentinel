# IOA system currently implementing
We are implementing IOA system right now, all the codes we did are in the IOA folder.

## Approach


## How to run
Run the Main.java class with following parameters
### train
Train the system with trainDataset.txt which is a file in /resources/tweets/. Attention please donnot add the ".txt" suffix of the dataset.
<br />[parameters] train trainDataset 
### evaluation
Evaluate the system with testDataset.txt which is a fle in /resources/tweets/. Attention please donnot add the ".txt" suffix of the dataset.
<br />[parameters] eval testDataset 
### output
System will output the result(including good prediction and bad prediction) to /output/result.txt, output the errors to /output/error\_analysis/error.txt
### Examples
    "I drove a Linconl and it's a truly dream" 
    Linconl -> proper noun -> positive

    "I drove a Linconl and it was awful"
    Linconl -> proper noun -> negative
    
