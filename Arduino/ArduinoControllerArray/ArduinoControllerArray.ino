#include <Adafruit_NeoPixel.h>

#define PIN            6
#define NUMPIXELS     74

 unsigned int values[3];
 unsigned int array[NUMPIXELS];
 int arrayValue = 0;
 int i = 0;

Adafruit_NeoPixel pixels = Adafruit_NeoPixel(NUMPIXELS, PIN, NEO_GRB + NEO_KHZ800);

void setup() {
  Serial.begin(9600);
  pixels.begin();
  pixels.show();
}

void loop() {
  if (i == 3) {
      int temValue = 0;
      while(temValue < arrayValue){
        pixels.setPixelColor((array[temValue]),values[0],(values[1]),(values[2]));
        temValue++;
      }
      pixels.show();
      arrayValue = 0;
      i = 0;
    
 }else{
  if(Serial.available() > 0){
      unsigned int waarde = Serial.read();
      if(waarde > 128){
        array[arrayValue] = waarde-129;
        arrayValue++;
      }else{
        values[i] = (waarde-10)*4;
        i++;
      }
  }
 }
 
}
