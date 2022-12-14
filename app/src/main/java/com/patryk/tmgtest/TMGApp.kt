package com.patryk.tmgtest

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TMGApp : Application() {

    override fun onCreate() {
        super.onCreate()

    }
}

//https://gist.githubusercontent.com/patrykpoborca/5f8abb88040d70f57528c9aa0364fecb/raw/9ee6968b94df0594d1a79335c1c0f3f1ffceeaee/tmgdata.json
/*
[
   {
      "p1":"Amos",
      "score1":4,
      "p2":"Diego",
      "score2":5
   },
   {
      "p1":"Amos",
      "score1":1,
      "p2":"Diego",
      "score2":5
   },
   {
      "p1":"Amos",
      "score1":2,
      "p2":"Diego",
      "score2":5
   },
   {
      "p1":"Amos",
      "score1":0,
      "p2":"Diego",
      "score2":5
   },
   {
      "p1":"Amos",
      "score1":6,
      "p2":"Diego",
      "score2":5
   },
   {
      "p1":"Amos",
      "score1":5,
      "p2":"Diego",
      "score2":2
   },
   {
      "p1":"Amos",
      "score1":4,
      "p2":"Diego",
      "score2":0
   },
   {
      "p1":"Joel",
      "score1":4,
      "p2":"Diego",
      "score2":5
   },
   {
      "p1":"Tim",
      "score1":4,
      "p2":"Amos",
      "score2":5
   },
   {
      "p1":"Tim",
      "score1":5,
      "p2":"Amos",
      "score2":2
   },
   {
      "p1":"Amos",
      "score1":3,
      "p2":"Tim",
      "score2":5
   },
   {
      "p1":"Amos",
      "score1":5,
      "p2":"Tim",
      "score2":3
   },
   {
      "p1":"Amos",
      "score1":5,
      "p2":"Joel",
      "score2":4
   },
   {
      "p1":"Joel",
      "score1":5,
      "p2":"Tim",
      "score2":2
   }
]
 */