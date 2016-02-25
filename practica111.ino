#include <SoftwareSerial.h>

SoftwareSerial bt(10, 11); // RX, TX
int verde = 3;
int amarillo = 4;
int rojo = 5;
int suma;
float temperatura = 0; //variable para la temperatura
int temp;
int elec;
int giro;
int sensorElectro;

void setup(){
  Serial.begin(9600);
  bt.begin(9600);
  pinMode(verde, OUTPUT);
  pinMode(amarillo, OUTPUT);
  pinMode(rojo, OUTPUT);
  //int sensorElectro = analogRead(A1);
}
void loop(){
  
    //int sensorTemp = analogRead(A3);
    //int sensorElectro = analogRead(A0);
    //int sensorPosicion = analogRead(AA);
    
     sensorElectro = analogRead(A1);
temperatura = (5.0 * analogRead(0)*100.0)/943.0;
Serial.println (temperatura); //escribe la temperatura en el serial
//delay (500);
    
if(temperatura>30 && temperatura<39){
  temp=1;
}else{
  temp=0;
}

if(sensorElectro>0){
  elec=1;
}else{
  elec=0;
}
      giro= bt.read() - 48;
      Serial.println(giro);
       
suma=temp+elec+giro;      
      switch(suma){
        case 1:
        digitalWrite(rojo, HIGH);
        digitalWrite(amarillo, LOW);
        digitalWrite(verde,LOW);
        //delay(1000);
        break;
        
        case 2:
        digitalWrite(amarillo,HIGH);
        digitalWrite(rojo,LOW);
        
        digitalWrite(verde,LOW);
        //delay(1000);
        break;
        
        case 3:
        digitalWrite(verde,HIGH);
        digitalWrite(amarillo,LOW);
        digitalWrite(rojo,LOW);
        //delay(1000);
        break;
        
        default:
        break;
      } 


      
     
    Serial.println(sensorElectro);

    //Serial.println(sensorPosicion);
}
