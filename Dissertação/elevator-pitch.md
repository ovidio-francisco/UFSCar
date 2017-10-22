
# Mestrado

Dado um documento com texto em linguagem natural, identificar os assuntos que são tratados nesse documento.



Pegar um documento com vários assuntos e dizer: esse trecho fala desse assunto, esse trecho fala desse outro assunto.
"Imagine duas pessoas que conversaram no Whatsapp por 1 hora. A ideia é dizer algo como: Nas primeiras mensagens (1-30) conversaram (predominantemente) sobre política, em seguida, das mensagens (31-50) o assunto passou para esporte, depois, nas mensagens (51-80) falaram sobre religião, e terminaram conversando sobre um filme."	

  --> Empregar técnicas de Recuperação de informação para encontrar histórico de assuntos mencionados em atas de reunião.



--> Como faz?

	- Extrair texto
	- Segmentar texto (corpo da ata (sem cabeções e rodapés)
	- Salvar cada segmento da ata em sub-documentos
	- Preprocessar o texto
	- Extrair tópicos
	- Usar os tópicos na consulta com o usuário
	- Apresentar os resultados ao usuário



É um sistema de recuperação de informação

temos 6 atas que 2 professores segmentaram, e para cada segmento rotularam e escolheram os termos que melhor descrevem cada segmento.

