# MVVM-Contacts-List-App
Show phone contact list and call log history in Recyclerview using MVVM with data binding, repository and Kotlin Coroutines

## The app is written in kotlin and uses:

* AndroidX packages.
* Data binding.
* Kotlin Coroutines for background operations.


## The app has following packages:

* Model - contains data entities classes which represents each contacts and call log history. 
  Also contains repositories classes which handle the search processes and help with abstracting the data layer from the viewModel.
* view- main activity and fragments with a recyclerView adapter for binding data.
* utils - binding adpter class for view binding.
* viewModel - conatins viewModel class for storing and managing UI ralated data. 
  also contains viewModel factory classes which help construct ViewModel with parameter.

## The app follows the Android Architectue Components desgin and uses:

* LiveData.
* ViewModel.
* Repository pattern.
* LifeCycle.

## Uses of third-party libraries in the app:

* Glide library for image loading and caching support.

## Common design patterns used in this app:

* Observer: It allows objects to be notified of any change of state of another object on which it depends. 
  presents in android viewModel
* Adapter: It allows two incompatible classes can work together. When creating a list of data, to inflate in a RecyclerView the Adapter   is used to send data from a controller to a particular view.
* ViewHolder: Used in the Adapters to reuse views in the inflation process of the cells. This brings us main benefit the proper    	       management of memory and fluency in lists. it's a mendatory in RecyclerView.

## Design Principles implemented are:

* DRY (Don’t Repeat Yourself)
* SRP: “Single Responsibility Principle” (part of SOLID principles)
* "Separation of concerns"
* "Dependency inversion principle"
