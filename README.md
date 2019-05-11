# Emotion_Butler
The Android application “Emotion Butler” is built with Android Studio, with SDK of Empatica E4 wristband for physiological signal input, especially the ibi(interbeat interval) values for conversion into heart rate values by 60/ibi as bpm(heart per minute). 

The mobile application contains several functions to support users in urgent situation, in short term and in long term.

-----------------------------------
Short term function:

* Realtime Care:
The core functioning of Emotion Butler is “Realtime Care”, which works with Empatica E4 wristband and receives user’s real time heart rate, then compares it with the baseline heart rate. If the heart rate value is much higher than baseline heart rate value, the application would query the user whether they need to handle stress; if the reason of heart rate increasing is irrelevant to stress, the user could easily dismiss that query, otherwise the application would provide a breath training activity to help the user relax. 

* Breath Training:
During the breath training, which performs “4-7-8 technique”, users can see the heart rate value on the screen to know the progress. 

* Baseline Heart Rate Setting:
Also, since the baseline heart rate for each person varies, there is also a function keeping each person’s baseline heart rate value profile separately, so the “Realtime Care” function works with different thresholds depending on different people.

-----------------------------------
Long term function:

* Mood Journal:
Users could use “Mood Journal” for recording of their mood and thoughts, and they could review and edit them readily. 


* Power Pool:
For motivation, there is “Power Pool” function that user could input encouraging personal tips for themselves. 


* Positive Thinking:
Since sometimes people who struggled with depression might tend to think negatively such as unnecessary self-blaming, the function of “Positive Thinking” helps user to rethink and relieve the stress.

-----------------------------------
Urgent situation:

* Heart broken button:
Furthermore, there is a button that is available in every layout of application, the “heart broken button”; this function is aiming to help people who are having suicidal thoughts and hopefully to support them and to suggest them to call national suicide prevention hotline.

