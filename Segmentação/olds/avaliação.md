# Avaliação

	6 documentos
	2 especialistas
	N modelos
	
	
# A referência
	Como referência de segmentação, seis atas foram oferecidas a dois professores com conhecimento a respeito dos assutnos discutinos nas reuniões. Utilizando um software o professores, aqui chamados de especialistas, leram e segmetaram as atas conforme sua percepção, e entregaram os segmentos referentes a cada ata. 
	
	
	
	
	
	
	
	
	
	
	de acordo com 





 a eficácia de dois algoritmos é significativamente diferente sempre que seus correspondentes rankings médio diferirem
por pelo menos um determinado valor de diferença crı́tica (CD).





2 etapas

	1ª etapa
		- extrai o melhor modelo de cada algoritmo para cada medida
	
	2ª etapa
		
	
	
	
	
		- escolhe-se o modelo que mais se repete em primeiro lugar nas seis medidas.




===============================================================================================


A Figura~\ref{fig:cd} 


\newcommand{\cdsize}{0.5\textwidth}

\begin{figure}[!h]
	\centering
	\includegraphics[width=\cdsize]{CD/WinDiff-2.png}
	\caption{Diagramas de diferença crítica do pós-teste de Nemenyi}
	\label{fig:cd}
\end{figure}






\newcommand{\cdsize}{0.6\textwidth}

\begin{figure}[!h]
	\centering
	
	\begin{subfigure}{\cdsize}	
		\centering
		\includegraphics[width=\cdsize]{CD/Acuracy-2.png}
		\caption{Acurácia}
	\end{subfigure}
	
	\begin{subfigure}{\cdsize}	
		\centering
		\includegraphics[width=\cdsize]{CD/F1-2.png}
		\caption{F1}
	
	\end{subfigure}

	\begin{subfigure}{\cdsize}	
		\centering
		\includegraphics[width=\cdsize]{CD/Pk-2.png}
		\caption{P$_k$}
	
	\end{subfigure}

	\begin{subfigure}{\cdsize}	
		\centering
		\includegraphics[width=\cdsize]{CD/Precision-2.png}
		\caption{Precision}
	
	\end{subfigure}


	\begin{subfigure}{\cdsize}	
		\centering
		\includegraphics[width=\cdsize]{CD/Recall-2.png}
		\caption{Recall}
	
	\end{subfigure}

	\begin{subfigure}{\cdsize}	
		\centering
		\includegraphics[width=\cdsize]{CD/WinDiff-2.png}
		\caption{WindowDiff}
	
	\end{subfigure}



	\caption{Diagramas de diferença crítica do pós-teste de Nemenyi}
	\label{fig:cd}
\end{figure}





comparando-se o seu resultado com a segmentação fornecida pelos especialistas.
	
Os modelos foram avaliados comparando-se a segmentação automática com a segmentação fornecida pelos especialistas 





%Para contornar essas dificuldades, algumas abordagens podem ser utilizadas. Algumas autores preferem detectar a segmentação em textos formados pela concatenação de documentos distintos, para que não haja diferenças subjetivas \cite{Reynar 1994; Choi 2000; e própiro autor do "A Critique and Impro.}. Há ainda outros que não avaliam o algoritmo diretamente, mas seu impacto na aplicação final\cite{Manning 1998; Kan,
%Klavans, and McKeown 1998}. 
%Outras abordagens apenas atribuiem um segmento cada quebra de parágrafo \cite{Two Step ... Meeting Minutes}







%avaliação
%todos precisam de um gold text
%
%	1 - Concatenação
%	2 - Juízes concordam ou não 
%	3 - Mediador na reunião
%	4 - Não avaliar o segmentador e sim o resultado da aplicação final.
%	5 - Consultar o autor do texto











Os modelos foram avaliados comparando-se a segmentação automática com a segmentação fornecida pelos especialistas. 



%O \textit{TextTiling}, permite ajustarmos os valores para o tamanho da janela (distância entre a primeira e a última sentença) e o passo (distância que a janela desliza a cada iteração). 
%%
%Os valores atribuídos aos parâmetros formam 
%%
%O \textit{C99} permite ajustarmos a quantidade de segmentos desejados, o tamanho da máscara utilizada para gerar a matriz de ranking e definirmos se as sentenças serão representados por vetores contendo a frequência ou o peso de cada termo.

%
%Os valores dos parâmetros foram combinados 




% Atribuiu-se 


%A seguir, e apresentado os modelos avaliados
%
%TextTiling:
%	1: Tamanho da janela: de 20 a 60 acrescentando 10    --> [20, 40, 60]
%	2: Passo: de 3 a 12 acrescentando 3                  --> [3, 6, 9, 12]
%	
%C99:
%	1: Quantidade de sementos desejados: 0,2 a 1,0       --> [0,2 0,4, 0,6, 0,8 1,0]
%	2: Tamanho de raking 9 a 11                          --> [9 11]
%	3: Weigth: true e false                              --> [T F]
%	
	

