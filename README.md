# plugfest-tooling
   
   To compile a demo:
     ./gradlew app:clean app:build

   To run cli demo for the differ:
     java -jar app/build/libs/app.jar p1.spdx p2.spdx 

       console output:

            < p2.spdx : > p1.spdx
            -----
            1
            =====
            < SPDXVersion: SPDX-2.3
            > SPDXVersion: SPDX-2.2

            10
            =====
            < Created: 2023-02-21T13:52:11Z
            > Created: 2023-02-21T13:52:11Z-pliu
