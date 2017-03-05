
# Pauta



##    Os algoritmos TextTiling e C99

	- Um agoritmo identifica e marca os finais de sentença
	- Os segmentadores usam os finais de sentença como "Candidatos a Limites de Segmento".
	- TextTiling recebe como parâmetros o tamanho da janela e o tamanho do passo.
	- C99 recebe tambem a quantidade de segmentos desejado.


##	  Medidas de Segmentação

		- As sentenças precisam ser idênticas tanto para o especialista, quanto para o algoritmo. 
			- O algoritmo (que identifica finais de sentença) não é perfeito.
			- O especialista é imprevisível.
			- A quantidade de FN é muito alta.

				- fazer com que o total de sentencas seja:
					- as sentecas marcadas pelo algoritmo +
					- as senteças que foram marcadas só pelo especialista.



##	  Cronograma    		



