# Identifying the Gender of a Voice using Machine Learning
# We measures 20 acoustic parameters on acoustic signals.
# Files names, duration and peak frequency (peakf) were removed from training.  


# here are some instructions for installing required packages:
# packages <- c('tuneR', 'seewave', 'fftw', 'caTools', 'warbleR', 'mice', 'e1071', 'rpart', 'e1071')
# if (length(setdiff(packages, rownames(installed.packages()))) > 0) {
#   install.packages(setdiff(packages, rownames(installed.packages())))  
# }

library(tuneR) #
library(seewave) #
library(caTools) #
library(rpart)
library(maps)
library(warbleR) #
library(mice) #
library(e1071) #

#입력 데이터 주소 지정
setwd('\\Users\\ljh99\\Desktop\\GRV\\Gender_Recognition_by_Voice\\grv\\dataset\\input_data')
getwd()
specan3 <- function(X, bp = c(0,22), wl = 2048, threshold = 15, parallel = 1){
  sound.files <- as.character(unlist(X$sound.files))
  
  x <- as.data.frame(lapply(1:length(sound.files), function(i) { 
    r <- tuneR::readWave(file.path(getwd(), sound.files[i]), from = 0, units = "seconds") 
    b <- bp #in case bp its higher than can be due to sampling rate
    if (b[2] > ceiling(r@samp.rate/2000) - 1) {b[2] <- ceiling(r@samp.rate/2000) - 1}
    
    
    #frequency spectrum analysis
    songspec <- seewave::spec(r, f = r@samp.rate, plot = FALSE)
    analysis <- seewave::specprop(songspec, f = r@samp.rate, flim = c(0, 280/1000), plot = FALSE)
    
    #save parameters
    meanfreq <- analysis$mean/1000
    sd <- analysis$sd/1000
    median <- analysis$median/1000
    Q25 <- analysis$Q25/1000
    Q75 <- analysis$Q75/1000
    IQR <- analysis$IQR/1000
    skew <- analysis$skewness
    kurt <- analysis$kurtosis
    sp.ent <- analysis$sh
    sfm <- analysis$sfm
    mode <- analysis$mode/1000
    centroid <- analysis$cent/1000
    
    #Frequency with amplitude peaks
    #Peakf was omitted from calculation due to time and CPU constraints in calculating the value.
    peakf <- 0 #seewave::fpeaks(songspec, f = r@samp.rate, wl = wl, nmax = 3, plot = FALSE)[1, 1]
    
    
    #Fundamental frequency parameters
    ff <- seewave::fund(r, f = r@samp.rate, ovlp = 50, threshold = threshold, 
                        fmax = 280, ylim=c(0, 280/1000), plot = FALSE, wl = wl)[, 2]
    meanfun<-mean(ff, na.rm = T)
    minfun<-min(ff, na.rm = T)
    maxfun<-max(ff, na.rm = T)
    
    #Dominant frequency parameters
    y <- seewave::dfreq(r, f = r@samp.rate, wl = wl, ylim=c(0, 280/1000), 
                        ovlp = 0, plot = F, threshold = threshold, bandpass = b * 1000, fftw = TRUE)[, 2]
    meandom <- mean(y, na.rm = TRUE)
    mindom <- min(y, na.rm = TRUE)
    maxdom <- max(y, na.rm = TRUE)
    dfrange <- (maxdom - mindom)
    duration <- seewave::duration(wave = r, f = r@samp.rate);
    
    #modulation index calculation
    changes <- vector()
    for(j in which(!is.na(y))){
      change <- abs(y[j] - y[j + 1])
      changes <- append(changes, change)
    }
    if(mindom==maxdom) modindx<-0 else modindx <- mean(changes, na.rm = T)/dfrange
    
    #save results
    return(c(duration, meanfreq, sd, median, Q25, Q75, IQR, skew, kurt, sp.ent, sfm, mode, 
             centroid, peakf, meanfun, minfun, maxfun, meandom, mindom, maxdom, dfrange, modindx))
  }))
  
  #change result names
  
  
  x <- data.frame(sound.files, as.data.frame(t(x)))
  colnames(x) <- c("sound.files","duration", "meanfreq", "sd", "median", "Q25", "Q75", "IQR", "skew", "kurt", "sp.ent", 
                   "sfm","mode", "centroid", "peakf", "meanfun", "minfun", "maxfun", "meandom", "mindom", "maxdom", "dfrange", "modindx")
  rownames(x) <- c(1:nrow(x))
  return(x)
}


processFolder <- function(folderName) 
{
  # Start with empty data.frame.
  data <- data.frame()
  
  # Get list of files in the folder.
  list <- list.files(folderName, '\\.wav')
  
  # Add file list to data.frame for processing.
  for (fileName in list) 
  {
    row <- data.frame(fileName)
    data <- rbind(data, row)
  }
  
  # Set column names.
  colnames(data) <- c('sound.files')
  
  # Move into folder for processing.
  setwd(folderName)
  
  # Process files.
  acoustics <- specan3(data, parallel=1)
  label = rep(folderName, nrow(acoustics))
  acoustics <- cbind(acoustics,label)
  
  # Move back into parent folder.
  setwd('..')
  
  acoustics
}


# Load data
voice <- processFolder('voice')

voice <- rbind(voice)
View(voice)
drops <- c("sound.files","duration","peakf","label")
voice <- voice[ , !(names(voice) %in% drops)]
View(voice)
dim(voice)
nrow(voice)
#출력 데이터 주소 지정
write.csv(voice, file = '\\Users\\ljh99\\Desktop\\GRV\\Gender_Recognition_by_Voice\\grv\\dataset\\input_data\\output\\voice.csv', row.names = FALSE)
















