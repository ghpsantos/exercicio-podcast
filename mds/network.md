## Network

Para essa categoria, foi realizada a análise de comportameto da rede para a aplicação de podcast.

Ao iniciar a aplicação, o download da lista de episódios é realizado, dessa forma, um pequeno pico ocorreu. A Figura 5 mostra essa variação. Após o download da lista de episódios, nenhuma alteração ocorre.

| ![figura5.jpg](https://github.com/ghpsantos/exercicio-podcast/blob/master/screenshots/figura5.jpg) | 
|:--:| 
| *Figura 5* |

Ao minimizar a aplicação e retornar para a mesma, um pequeno pico ocorre, da mesma forma que na primeira abertura da aplicação, já que no onStart da MainActivity o download da lista é sempre realizado, com o intuito de recuperar a lista mais atualizada. A Figura 6 mostra o momento exato em que a activity é colocada em primeiro plano, fazendo com que o método onStart seja disparado e com isso o download da lista mais atual.

| ![figura6.jpg](https://github.com/ghpsantos/exercicio-podcast/blob/master/screenshots/figura6.jpg) | 
|:--:| 
| *Figura 6* |

A Figura 7 mostra o fluxo de que ocorre durante o download após ele ser iniciado.

| ![figura7.jpg](https://github.com/ghpsantos/exercicio-podcast/blob/master/screenshots/figura7.jpg) | 
|:--:| 
| *Figura 7* |

Tendo o download finalizado, o tráfego de dados volta a ser 0 MB/S. Além disso, o texto do botão é alterado para “Ouvir”, não precisando ser feito o download da lista inteira. Dessa forma não houve alteração.

| ![figura8.jpg](https://github.com/ghpsantos/exercicio-podcast/blob/master/screenshots/figura8.jpg) | 
|:--:| 
| *Figura 8* |

Da mesma forma que em primeiro plano, quando é colocado para fazer o download, e a aplicação é retirada do primeiro plano, o tráfego de dados continua o mesmo, visto que este depende apenas do serviço que está sendo rodado em background.


## Network Após Architecture Components

