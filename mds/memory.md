## Memória

Para essa categoria, foi realizada a análise de comportameto do consumo de memória para a aplicação de podcast.


Foi possível notar o consumo de memória para manter a aplicação em primeiro plano. Na Figura 1, é evidente o salto dado.
Após a abertura da aplicação, o consumo de memória ficou estável em 54,46 MB.

| ![figura1.jpg](https://github.com/ghpsantos/exercicio-podcast/blob/master/screenshots/figura1.jpg) | 
|:--:| 
| *Figura 1* |

Tendo deixado tela do dispositivo desligar, e acordá-la novamente através do desbloqueio, foi possível ver um aumento para 61,47 MB de memória para se manter estável como mostrado na Figura 2.

| ![figura2.jpg](https://github.com/ghpsantos/exercicio-podcast/blob/master/screenshots/figura2.jpg) | 
|:--:| 
| *Figura 2* |

Na Figura 3 vemos que ao iniciar o download de um mp3. Outro aumento no consumo de memória é feito, uma vez que um service foi ativado.

| ![figura3.jpg](https://github.com/ghpsantos/exercicio-podcast/blob/master/screenshots/figura3.jpg) | 
|:--:| 
| *Figura 3* |


Após o download terminar por parte do service, a aplicação diminui o consumo de memória e volta para o consumo normal.

A Figura 4 mostra a variação do consumo de memória quando a aplicação inicia o download, e logo após é tirada de primeiro plano, ficando, assim em memória enquanto o service continua a fazer o download.

| ![figura4.jpg](https://github.com/ghpsantos/exercicio-podcast/blob/master/screenshots/figura4.jpg) | 
|:--:| 
| *Figura 4* |

Além disso, foi usada a ferramente LeakCanary para a detecção de leaks de memória. Felizmente nenhum leak foi encontrado na aplicação, sendo esta utilizada de várias formas para que pudesse apresentar algum gargalo.

| ![figura4.jpg](https://github.com/ghpsantos/exercicio-podcast/blob/master/screenshots/figura13.jpg) | 
|:--:| 
| *LeakCanary* |

| ![figura4.jpg](https://github.com/ghpsantos/exercicio-podcast/blob/master/screenshots/figura14.jpg) | 
|:--:| 
| *LeakCanary* |

| ![figura4.jpg](https://github.com/ghpsantos/exercicio-podcast/blob/master/screenshots/figura15.jpg) | 
|:--:| 
| *LeakCanary* |
