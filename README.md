Random Pics
===========

Author: Pei Lian Liu
Email: epsonavy@yahoo.com

The android app can generate random pictures and search keyword to get the result pictures.
There are two ways to extract the JSON:

~~~~~~
	// Ion for URL loading
    compile 'com.koushikdutta.ion:ion:2.+'

    // Use GSON and retrofit
    compile 'com.google.code.gson:gson:2.6.2'
    compile 'com.squareup.retrofit2:retrofit:2.1.0'
    compile 'com.squareup.retrofit2:converter-gson:2.1.0'
~~~~~~

Using Picasso to load pictures asynchronously

~~~~~~
    // Picasso http://square.github.io/picasso/
    // Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(imageView);
    compile 'com.squareup.picasso:picasso:2.5.2'
~~~~~~~~

Pictures from API Unsplash

- Unsplash is a website that provides hi-res images. 
> The API documentation is here https://unsplash.com/documentation


To run this application, you must use Android Studio. 
When opening Android Studio, it will ask you if you want to import a project, please choose that.
Select the directory random-pics-epsonavy/ from the files in the repository. Enjoy!

This application supports both horizontal and vertical modes

Notes
- Recommend use 5.2” Screen or above
- Only allow to send 100 times of search or random request due to the API limit


