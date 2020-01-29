# LAB2-ARSW

## Part I - Before finishing class
1. Check the operation of the program and run it. While this occurs, run jVisualVM and check the CPU consumption of the corresponding process. Why is this consumption? Which is the responsible class?
Este consumo se debe a que se estan ejecutando dos hilos al mismo tiempo constantemente. La clase responsable del mal uso de la CPU es la clase de consumidor ya que esta se encuentra constantemente ejecutandose incluso 
cuando no hay algun elemento que extraer.
[](img/consumption1.png)
