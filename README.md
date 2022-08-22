# PCM App Description:
### About:
**Welcome**
I am Mostafa Nema and this is the PCM assistant Application
I work as a technical examiner in the surface engineering department of Al-Waha Oil Company, which operates the Al-Ahdab oil field / Wasit / Iraq
I work in the PCM team to check the external corrosion of oil pipes
I did this work to reduce the effort and save time in making long reports and to ensure more quality and accuracy to save information.

The work of the PCM has always been tiring and exhausting, as we work in various parts of the field and travel long distances on foot daily to check the oil lines accurately and look for potential corrosion points, as an average of  150 kilometers  of pipelines are checked annually 
However, we return to the office or home, as in the past year, to work on writing reports and saving the information that was recorded on the same day

**In normal work:**
- Points are recorded in the word.docx file as an average of 30 points.
- Then it is converted to an excel file. to apply specific equations and calibrate the GPS coordinates to match the maps we work with.
- Then this coordinate is converted to a DWG map on AutoCAD to be projected and the path is drawn with the points specified on it.
      It is sometimes annoying and difficult and requires a lot of scrutiny to maintain accuracy because it is very important

<<**So the reason for this application is to facilitate the task of preparation and help save a lot of time**>>

**Where the application does the following:**
- Full oil line data registration and points registration and saving them to a local database.
- Taking GPS coordinates of the site with a high accuracy of up to 2 meters.
- Exporting the information in the form of a ready-made excel file containing the information and data of the oil line with a table of points and their information and coordinates for each point and they have been calibrated to match the AutoCAD maps.

All you have to do is record the information, then save it in the application and export it to an excel file. You can copy the coordinates and drop them directly on the map to draw the path.


**Average time taken to record data:>>**
(30 points)
- normal work  30 minutes .
- In the application  10 minutes.

**Average time to write single point:>>**
- Recording it on the regular worksheet and using the GPS device <b>5 minutes</b> GPS accuracy 5 meters.
- Recording it in the application and taking the coordinates through the application <b>1 minute</b>  GPS accuracy 2 meters.


**Average time taken to write the report:>>**
(30 points)
- normal work  1 hour.
- In the application  2 minutes.


### Technologies
- Kotlin
- Room Database.
- MVVM Architecture.
- viewBinding.
- dataBinding.
- floatingactionbutton:1.10.1
- ServiceLocator pattern.
- Dependency Injection
- Navigation
- GPS:LocationManager\LocationListener.
- BottomSheet.
- RecyclerView.
- Animition.
- Coroutines.
- WRITE TO EXTERNAL STORAGE.
- HSSFWorkbook.
- FileProvider.
- local unit tests.
- instrumented unit tests.
- Test Doubles
- MainCoroutineRule.
- InstantTaskExecutorRule.


### What Does Pipeline Current Mapper (PCM) Mean?
A pipeline current mapper (PCM) is a corrosion monitoring type of equipment used to perform alternating current coating attenuation surveys. Results from this survey are then used to prevent pipeline corrosion.
A pipeline current mapper operates by measuring the electric current characteristics of a given section of an above-ground or underground pipe. The electric current over the entire length of the pipe is measured and charted. This useful information can be used to prevent corrosion 
The PCM technology is high level and with special equipment, but if we want to simplify the applicable principle: we connect an electric current with a specific frequency on the oil pipeline and we receive the signal from the surface of the earth by means of special receiving devices that capture the same frequency as the current
If you want complete information on the subject, you can visit the following links and preview some types of devices for this purpose
- https://epcmholdings.com/pipeline-current-mapping-survey \n\n- https://www.corrosionpedia.com/definition/3129/pipeline-current-mapper-pcm#:~:text=A%20pipeline%20current%20mapper%20(PCM)%20is%20a%20corrosion%20monitoring%20type,used%20to%20prevent%20pipeline%20corrosion.


### instructions
**steps to get started:** 
We connect the equipment at the beginning of the oil pipeline and record the following information in the application
**We start a new line and record the following information**
- Well name
- Well type
- height
- work starting date
- Names of the work team
- The main warehouse and the destination of the Pipeline ogm
- The current on the Pipeline
- Output current from the Pipeline
- The coordinates of the starting point
Then we go and start the examination along the line


**At each potential corrosion point, we record the following readings**
⁃ db Electricity Leakage Rate.
⁃ Outgoing current at the beginning and end of the Pipeline
⁃ GPS coordinates of the point


**When finished, we record the readings:**
⁃ Final Electricity current 
⁃ End point coordinates
⁃ End Work Date

This information is saved in the application, and when you want to prepare the report, we produce the excel file for the line. The application prepares it in advance and you can share it and send it to the computer directly and complete the map work by copying the coordinates from the file and dropping them directly on the map
finished ✅

