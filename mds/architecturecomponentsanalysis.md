## Memória

Podemos observar que após a alteração para Architecture Components, a houve um aumento considerável de memória para que a aplicação se mantivesse em primeiro plano. Não houve nenhum leak.

| ![figura20.jpg](https://github.com/ghpsantos/exercicio-podcast/blob/master/screenshots/figura20.jpg) | 
|:--:| 
| *Figura 20* |

| ![figura21.jpg](https://github.com/ghpsantos/exercicio-podcast/blob/master/screenshots/figura21.jpg) | 
|:--:| 
| *Figura 21* |

## CPU

Assim como na arquitetura anterior, após as modificações não houve diferença de comportamento em relação a CPU, sendo esta alterada apenas quando o usuário deseja fazer interações com a aplicação (com rolagem de barra). 

## NETWORK

A rede seguiu os padrões como vinha seguindo antes das modificações. Assim que a aplicação é iniciada, há uma pequena pertubação, visto que é feito um download.

Todas as métricas estão na figura abaixo:

| ![figura16.jpg](https://github.com/ghpsantos/exercicio-podcast/blob/master/screenshots/figura16.png) | 
|:--:| 
| *Figura 16* |

Quando a rolagem é realizada, a CPU tem uma leve pertubação:

| ![figura17.jpg](https://github.com/ghpsantos/exercicio-podcast/blob/master/screenshots/figura17.png) | 
|:--:| 
| *Figura 17* |

Ao tentar fazer um download, o comportamento é semelhante, visto que a natureza da aplicação continuou a mesma, dessa forma, quando clicamos pra fazer download, a CPU tem uma leve alteração para fazer o processamento do download e do onClick do botão. Após isso, o download é iniciado e a rede começa a ter pertubação durante o processo de download.

| ![figura18.jpg](https://github.com/ghpsantos/exercicio-podcast/blob/master/screenshots/figura18.png) | 
|:--:| 
| *Figura 18* |


Por fim, ao tirar a aplicação de primeiro plano, o download continua, porém o consumo de memória diminui bastante, visto que não é preciso mais manter a aplicação.

| ![figura19.jpg](https://github.com/ghpsantos/exercicio-podcast/blob/master/screenshots/figura19.png) | 
|:--:| 
| *Figura 19* |
