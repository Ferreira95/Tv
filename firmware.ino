#include<Wire.h>
#include <EEPROM.h>
#include <avr/wdt.h>

// Constantes do acelerômetro
#define MPU 0x68
#define A_R 16384.0
#define G_R 131.0
#define RAD_A_DEG 57.295779

// Constantes mantidas do código original

#define time1 200
#define zonaMorta 1200

#define TXREP 2000 // Taxa de repetição, equivalente a velocidade do mouse no firmware original

// Constantes de "eventos"

#define TOP 1
#define DOWN 2
#define LEFT 3
#define RIGHT 4

int acx, acy, acz;
int xref = 0;
int zref = 0;
const int led1 = A2;

// Configuração inicial

void setup()
{
  Wire.begin();
  Serial.begin (9600);
  pinMode(led1, OUTPUT);
  Wire.beginTransmission(MPU);
  Wire.write(0x6B);
  Wire.write(0);
  Wire.endTransmission(true);
  wdt_enable (WDTO_2S);
  calibration();
}

// Loop  da placa
// Carrega os dados do acelerômetro
// e dispara o "evento" referente

void loop() 
{
  delay(TXREP);

//  if (Serial.available() > 0)
//   serial();

  acelGiro();

  if (acx < (xref + zonaMorta)) //eixo x
    Serial.print(TOP);
  if (acx > (xref - zonaMorta)) //eixo x
    Serial.print(DOWN);
  if (acz < (zref + zonaMorta)) //eixo Z
    Serial.print(LEFT);
  if (acz > (zref - zonaMorta)) //eixo Z
    Serial.print(RIGHT);
 
}

void acelGiro()
{
  Wire.beginTransmission(MPU);
  Wire.write(0x3B);
  Wire.endTransmission(false);
  Wire.requestFrom(MPU, 14, true);
  acx = Wire.read() << 8 | Wire.read();
  acy = Wire.read() << 8 | Wire.read();
  acz = Wire.read() << 8 | Wire.read();
}

void calibration()
{
  for (int calib = 0; calib < 14; calib++)
  {
    acelGiro();
    xref = acx;
    zref = acz;
    digitalWrite(led1, HIGH);
    delay(200);
    digitalWrite(led1, LOW);
    delay(200);
  }
}

