# firebase-offlinequeries-android
Sample program for Offline Query capabilities of Firebase on Android

1. First fire a query to retrieve 4 items. You will see 4 child_added events and a value event for the 4 children.
2. Next, switch to airplane mode and wait for the app to report it has disconnected.
3. Now fire the query again. You will see the same results as before.
4. Next, execute a query for 2 items. You will see 2 child_added events, but no value event (since it hasn't seen this query before).
5. Now query for 6 items. You will see 4 child_added events, since that is all the data available offline.
6. Finally go back online. Once the connection is detected by Firebase, the final 2 child_added events and the value event fire.
