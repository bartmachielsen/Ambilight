#include <Adafruit_NeoPixel.h>

#define PIN            6
#define NUMPIXELS     74

 unsigned int values[4];
 int i = 0;

Adafruit_NeoPixel pixels = Adafruit_NeoPixel(NUMPIXELS, PIN, NEO_GRB + NEO_KHZ800);

void setup() {
  Serial.begin(38200);
  pixels.begin();
  for(int z = 0; z < NUMPIXELS; z++){
    pixels.setPixelColor(z,0,0,0);
  }

    pixels.show();
}

void loop() {
  if (i == 4) {
        pixels.setPixelColor(values[0]-1,(values[1]*2.0),(values[2]*2.0),(values[3]*2.0));
        pixels.show();
        i = 0;
 }else{
  if(Serial.available() > 0){
      unsigned int waarde = Serial.read();
      values[i] = waarde;
      i++;
  }
 }

}
