#include <ESP8266WiFi.h>
#include <FirebaseESP8266.h>
#include "Servo.h"
#define pinSensorA 0
#define pinSensorD 5
Servo servoD2;


#ifndef STASSID
#define STASSID "PixGo-projetos"
#define STAPSK  "p1xg0@g41t"
#endif

const char* ssid     = STASSID;
const char* password = STAPSK;
const char* host = "https://projeto-led-2b632-default-rtdb.asia-southeast1.firebasedatabase.app";
const char* token = "By03oZ2up5gie4FVgKA2dqllJa20TOKesbkhZIH6";

FirebaseData base;

int angulo =0;
int valor = 0;




void setup() {
  pinMode(pinSensorA, INPUT);
  servoD2.attach(2);
  Serial.begin(19200);
  WiFi.begin(ssid,password);
  while (WiFi.status()!=WL_CONNECTED){
  delay(900);
}
//statusConexao =true;
Firebase.begin(host,token);
}

void loop() {
  
  Serial.print("  Analogico:");
  Serial.print(analogRead(pinSensorA)); 
  Serial.print("  ");
valor = analogRead(pinSensorA);
Firebase.setString(base, "Led/StatusValor", valor);

  Serial.print("  Atuador:");
  if (valor > 0 && valor < 400) {
     //Serial.println("SOLENOIDE LIGADO");
     Firebase.setString(base, "Led/StatusUmidade", "Umido");
     //digitalWrite(pinSolenoide, HIGH);
  }else if (valor > 400 && valor < 800) {
   // Serial.println("SOLENOIDE DESLIGADO");
    Firebase.setString(base, "Led/StatusUmidade", "Moderada");
     //digitalWrite(pinSolenoide, LOW);
  }else if (valor > 800 && valor <= 1024) {
    //Serial.println("SOLENOIDE DESLIGADO");
    Firebase.setString(base, "Led/StatusUmidade", "Seco");
     //digitalWrite(pinSolenoide, LOW);
  }



  Firebase.get(base,"Led/StatusMotor");
  int motor = base.intData();

switch(motor){

  case 0:
  for (angulo = 0; angulo < 180; angulo += 1) { // Comando que muda a posição do servo de 0 para 180°
  servoD2.write(angulo); // Comando para angulo específico
   break;  
  }
  break;

  case 1:
  for (angulo = 180; angulo >= 1; angulo -= 5) { // Comando que muda a posição do servo de 180 para 0°
  servoD2.write(angulo); // Comando para angulo específico
    break;  
  }
  break;  
  }


}
