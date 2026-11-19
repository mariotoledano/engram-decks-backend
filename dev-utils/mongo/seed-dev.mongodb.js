// Local development seed only. Wipes decks and flashcards in engram-decks.
// Run from repo root:
//   mongosh "mongodb://localhost:27017/engram-decks" --file dev-utils/mongo/seed-dev.mongodb.js
// Compass: use engram-decks, open Mongosh, paste this entire file and run.
//
// Documents match Deck and Flashcard:
//   decks: name, description, tags, cover { type, value }, createdAt, updatedAt, lastTimeStudiedAt?
//   flashcards: deckIds[], front, back, tags, createdAt, updatedAt
// A flashcard can belong to several decks. Card counts are derived from deckIds.

const now = new Date();

function daysAgo(days, hours = 0) {
	return new Date(now.getTime() - (days * 24 + hours) * 60 * 60 * 1000);
}

const COVER = {
	terracotta: { type: "COLOR", value: "#C4622D" },
	forest: { type: "COLOR", value: "#2F5D50" },
	ink: { type: "COLOR", value: "#3D4F7C" },
	plum: { type: "COLOR", value: "#6B3A5D" },
	teal: { type: "COLOR", value: "#1F6F6A" },
	rust: { type: "COLOR", value: "#7A3E2E" },
	slate: { type: "COLOR", value: "#4A5568" },
	olive: { type: "COLOR", value: "#5C6B32" },
	wine: { type: "COLOR", value: "#7A3048" },
	navy: { type: "COLOR", value: "#2C4A6E" },
	clay: { type: "COLOR", value: "#8A5A2B" },
	pine: { type: "COLOR", value: "#245E45" },
};

const MANY_DECK_TAGS = [
	"archivo",
	"repaso",
	"transversal",
	"examen",
	"universidad",
	"conceptos",
	"memoria",
	"seed",
	"practica",
	"avanzado",
	"basico",
	"cultura",
	"ciencia",
	"humanidades",
	"fechas",
	"personas",
	"teorias",
	"vocabulario",
	"sintesis",
	"interdisciplinar",
];

const MANY_CARD_TAGS = [
	"archivo",
	"repaso",
	"transversal",
	"examen",
	"universidad",
	"conceptos",
	"memoria",
	"seed",
	"practica",
	"avanzado",
	"metacognicion",
	"atencion",
	"olvido",
	"consolidacion",
	"habitos",
	"planificacion",
	"sesgo",
	"plasticidad",
	"sinapsis",
	"dopamina",
	"estudio",
	"largo-plazo",
	"recuperacion",
	"interleaving",
];

const DECK_DEFS = [
	{
		key: "etiquetas",
		name: "Archivo de etiquetas",
		description:
			"Mazo pensado para desbordar la línea de etiquetas. Reúne temas sueltos de varias asignaturas y sirve para comprobar el truncado, la búsqueda por tag y un contador pequeño.",
		tags: MANY_DECK_TAGS,
		cover: COVER.plum,
		createdDaysAgo: 40,
		updatedDaysAgo: 1,
		studiedDaysAgo: 1,
		studiedHoursAgo: 3,
		cards: [
			{
				front: "Carta con muchas etiquetas: ¿qué comprueba en la interfaz?",
				back:
					"Comprueba que una sola tarjeta puede llevar una lista larga de tags sin romper la fila ni la ficha. Los tags van en minúsculas, como los normaliza la API, y deben seguir siendo buscables uno a uno aunque la línea visible se corte. También sirve para ver el estado de una carta que concentra casi todo el vocabulario de etiquetas del seed.",
				tags: MANY_CARD_TAGS,
			},
			{
				front: "¿Por qué conviene normalizar las etiquetas antes de guardarlas?",
				back:
					"Porque «Memoria», « memoria » y «MEMORIA» son la misma idea para quien estudia. La API recorta espacios y pasa a minúsculas, así que el seed ya las escribe normalizadas. Si no se hiciera, los filtros ALL/ANY y el contador de tags del mantenimiento contarían duplicados que el usuario percibe como uno solo.",
			},
		],
	},
	{
		key: "nombre-largo",
		name:
			"Compendio exhaustivo de preguntas de repaso para el examen final de historia cultural del Mediterráneo antiguo, desde la Edad del Bronce hasta la caída de Roma occidental y sus ecos en la Edad Media temprana",
		description:
			"El nombre ocupa más de dos líneas a propósito, para ver el truncado del título en la tarjeta del mazo y en la cabecera de estudio. La descripción también es larga: mezcla cronología, geografía y método. No es un temario completo, solo material suficiente para llenar la ficha y obligar al texto a cortarse.",
		tags: ["historia", "mediterraneo", "seed", "examen", "nombre-largo"],
		cover: COVER.clay,
		createdDaysAgo: 38,
		updatedDaysAgo: 2,
		studiedDaysAgo: 6,
		cards: [
			{
				front:
					"¿Por qué la Edad del Bronce del Mediterráneo no puede contarse como una sola cultura homogénea, y qué cambia si se mira el intercambio en lugar de los reinos?",
				back:
					"Egeo, Levante, Egipto y el mundo hitita compartían metales, motivos y rutas, pero no una misma organización política ni la misma escritura. Mirar el intercambio —estaño, cobre, cerámica, textos diplomáticos— muestra una red de puertos y palacios que se sostiene por el comercio a larga distancia. Cuando esa red se contrae, hacia el final del Bronce, varias sociedades palaciales caen a la vez sin que haga falta una única causa militar. La pregunta larga también sirve para ver cómo se parte el anverso en la pantalla de estudio.",
			},
			{
				front: "¿Qué deja Roma en el Mediterráneo cuando cae la mitad occidental del imperio?",
				back:
					"No desaparece una administración de un día para otro. Quedan latín escrito, derecho de propiedad, calzadas, obispados y la idea de una autoridad que reclama continuidad con el imperio. En Occidente el poder se fragmenta en reinos que aún se presentan como herederos de Roma; en Oriente el imperio sigue, con Constantinopla como centro. El «eco medieval» de este mazo es esa continuidad parcial, no una copia intacta de la ciudad clásica.",
			},
			{
				front: "¿Qué es útil memorizar de una cronología tan ancha y qué es mejor dejar como relato?",
				back:
					"Fechas bisagra, nombres de procesos y un mapa mental de regiones. El detalle narrativo se olvida si no se recupera, y este mazo existe precisamente para practicar esa recuperación con textos más largos que una definición de una línea.",
			},
		],
	},
	{
		key: "vacio",
		name: "Mazo vacío",
		description:
			"Este mazo no tiene ninguna carta a propósito. Sirve para comprobar el contador en cero, la ficha sin listado y la pantalla de estudio cuando no hay nada que repasar. No enlaces aquí la carta compartida.",
		tags: ["vacio", "seed", "prueba"],
		cover: COVER.slate,
		createdDaysAgo: 12,
		updatedDaysAgo: 12,
		cards: [],
	},
	{
		key: "estoica",
		name: "Filosofía estoica",
		description:
			"Marco Aurelio, Epicteto y Séneca, leídos como práctica y no solo como cita. El mazo insiste en la dicotomía del control, el juicio y la diferencia entre virtud e indiferentes. Los reversos son párrafos, para probar tarjetas que no caben en una sola pantalla.",
		tags: ["filosofia", "estoicismo", "seed", "etica"],
		cover: COVER.terracotta,
		createdDaysAgo: 60,
		updatedDaysAgo: 3,
		studiedDaysAgo: 0,
		studiedHoursAgo: 5,
		cards: [
			{
				front: "¿Qué es la dicotomía del control y qué error cotidiano corrige?",
				back:
					"Separa lo que depende de tu juicio y tu voluntad de lo que no depende de ti: el cuerpo, la fama, el clima, las decisiones ajenas. El error que corrige es tratar la reputación o el resultado como si fueran deberes morales. Epicteto lo formula así: no nos perturban las cosas, sino los juicios que hacemos de ellas. Practicarlo no es indiferencia afectiva; es dejar de pelear con hechos ya ocurridos y reservar el esfuerzo para la respuesta.",
			},
			{
				front: "¿En qué se distinguen un preferido indiferente y la virtud?",
				back:
					"Salud, riqueza y reputación son preferibles: un estoico no finge que dan igual en la vida práctica. Pero no son el bien. El bien es la virtud, la calidad del juicio y de la acción. Por eso la enfermedad es un indiferente rechazado, desagradable y a menudo grave, sin convertirse por ello en un mal moral. La distinción evita dos extremos: el ascetismo que desprecia el cuerpo y la idea de que perder el cargo es perderse a uno mismo.",
			},
			{
				front: "¿Qué trabajo hace la reseña nocturna?",
				back:
					"Recorrer el día ya cerrado: qué se dijo, dónde el juicio se adelantó al hecho, qué se puede ensayar mañana. Séneca y Marco Aurelio la tratan como oficio, no como culpa. Sirve para convertir la filosofía en una revisión corta y repetible, que es también lo que este mazo pide al estudiar: recuperar la idea, no reconocerla al verla.",
			},
			{
				front: "¿Qué significa que el obstáculo es el camino?",
				back:
					"Marco Aurelio observa que lo que estorba una acción puede convertirse en su materia: la demora entrena la paciencia, el interlocutor difícil entrena la justicia. No es optimismo. Es negarse a esperar un día sin fricción para empezar a actuar bien. El impedimento deja de ser solo interrupción y pasa a ser el ejercicio.",
			},
		],
	},
	{
		key: "programacion",
		name: "Programación",
		description:
			"Diseño, complejidad y HTTP con explicaciones de varios renglones. Útil para ver descripciones que se cortan en la lista de mazos y reversos que obligan a hacer scroll dentro de la carta.",
		tags: ["programacion", "software", "seed"],
		cover: COVER.ink,
		createdDaysAgo: 55,
		updatedDaysAgo: 4,
		studiedDaysAgo: 2,
		cards: [
			{
				front: "¿Qué cuenta la notación O grande y qué deja fuera a propósito?",
				back:
					"Describe cómo crece el tiempo o la memoria cuando el tamaño de la entrada tiende a infinito. Deja fuera constantes y términos de orden menor, porque a esa escala no cambian la forma de la curva. Por eso un algoritmo O(n log n) puede ser más lento que uno O(n²) con datos pequeños y aun así ser el que conviene cuando la colección crece. Leer «O(n)» como «rápido» sin mirar el tamaño real es el malentendido que esta tarjeta quiere deshacer.",
			},
			{
				front: "¿Qué protege el principio de responsabilidad única cuando el código todavía es pequeño?",
				back:
					"Una sola razón para cambiar. Aunque el módulo quepa en una pantalla, mezclar formato, reglas de negocio y acceso a datos hace que un cambio de presentación arrastre pruebas de dominio. La regla no pide una clase por línea: pide que el motivo del próximo cambio sea localizable. En una API como esta, el controlador transporta HTTP y el servicio decide; si el DTO se cuela en el documento de Mongo, las dos razones de cambio quedan atadas.",
			},
			{
				front: "¿En qué se diferencian PUT y PATCH cuando el cliente no envía todos los campos?",
				back:
					"PUT sustituye el recurso: lo que no viaja en el cuerpo deja de formar parte del estado nuevo, salvo que el contrato diga otra cosa. PATCH modifica solo lo indicado y conserva el resto. Enviar un PATCH con tres campos no debe borrar la descripción ni las etiquetas que el cliente no mencionó. Confundirlos produce mazos que pierden la portada o las tags al «actualizar» un nombre.",
			},
			{
				front: "¿Qué demuestra un test unitario y qué deja sin demostrar?",
				back:
					"Demuestra que una unidad, con dependencias sustituidas, cumple un comportamiento concreto. No demuestra que el cableado real, la base de datos o el contrato HTTP estén bien. Por eso un servicio mockeado puede estar verde y el seed, al insertar documentos con otra forma, descubrir el fallo que el unitario no ve.",
			},
		],
	},
	{
		key: "psicologia",
		name: "Psicología del aprendizaje",
		description:
			"Memoria, sesgos y motivación. Los textos son más largos que una definición de manual para poder estudiar de verdad y, de paso, llenar la tarjeta.",
		tags: ["psicologia", "aprendizaje", "seed", "memoria"],
		cover: COVER.teal,
		createdDaysAgo: 50,
		updatedDaysAgo: 5,
		studiedDaysAgo: 9,
		cards: [
			{
				front: "¿Qué muestra la curva del olvido y qué no debe concluirse de ella?",
				back:
					"Ebbinghaus mostró que la retención de material sin sentido cae rápido al principio y luego más despacio. No debe concluirse que «todo se olvida igual»: el significado, el sueño y el intento de recuperar la respuesta cambian la pendiente. La curva justifica el repaso espaciado, no la idea de que estudiar una vez es inútil o de que hace falta repetir todos los días lo mismo.",
			},
			{
				front: "¿En qué consiste el sesgo de confirmación cuando se corrige uno mismo?",
				back:
					"Se busca y se pesa antes la evidencia que encaja con lo que ya se cree. Al repasar, aparece como «ya me lo sabía» justo después de haber leído el reverso, o como saltarse la tarjeta que siempre se falla. Separar el intento de respuesta del descubrimiento de la solución reduce ese falso reconocimiento. El sesgo no se cancela por conocer su nombre; se reduce cambiando el procedimiento.",
			},
			{
				front: "¿Qué añade la teoría de la autodeterminación a «tener motivación»?",
				back:
					"Distingue tres necesidades: autonomía (elegir de un modo coherente con los propios valores), competencia (notar que se progresa) y relación (hacerlo con otros o para otros). Una racha de estudio que solo evita la culpa puede ser control externo. La misma hora de repaso se sostiene mejor si el mazo se eligió, la dificultad sube de forma visible y hay alguien con quien contrastar lo aprendido.",
			},
		],
	},
	{
		key: "neuro",
		name: "Neurociencia",
		description:
			"Neurona, memoria y sistemas que conviene no simplificar de más. Hay texto de sobra para ver el reverso en varias pantallas.",
		tags: ["neurociencia", "cerebro", "seed"],
		cover: COVER.wine,
		createdDaysAgo: 48,
		updatedDaysAgo: 6,
		studiedDaysAgo: 4,
		cards: [
			{
				front: "¿Qué hace el hipocampo con un recuerdo y qué no almacena por sí solo para siempre?",
				back:
					"Es necesario para formar muchas memorias episódicas y para situarlas en el espacio. Con el tiempo, parte de esa información se sostiene en redes corticales y el hipocampo deja de ser el único soporte. Una lesión hipocampal reciente borra lo recién consolidado y respeta mejor lo muy antiguo. Por eso «el recuerdo está en el hipocampo» es una frase cómoda y, a medio plazo, falsa.",
			},
			{
				front: "¿Qué es la potenciación a largo plazo si se explica sin reducirla a «las neuronas se acostumbran»?",
				back:
					"Es un aumento duradero de la eficacia de ciertas sinapsis después de una activación intensa y repetida. Cambia el peso de la conexión, no el hecho de que la neurona exista. Importa porque el aprendizaje no exige, en cada caso, una neurona nueva: basta con que el mismo circuito responda de otra manera la próxima vez. La depresión a largo plazo hace el trabajo inverso y también es aprendizaje.",
			},
			{
				front: "¿Por qué la dopamina no es simplemente la molécula del placer?",
				back:
					"Señala diferencia entre lo esperado y lo obtenido, y empuja a buscar y a iniciar la acción. Una comida rica puede liberarla antes de saborearla, cuando aparece la señal que la predice. Reducirla a placer mezcla recompensa, motivación y movimiento, que viajan por vías distintas. En el estudio, la anticipación de «cerrar el mazo» puede pesar más que la satisfacción de haber recordado la tarjeta.",
			},
		],
	},
	{
		key: "habitos",
		name: "Hábitos de estudio",
		description:
			"Cómo organizar el repaso cuando hay muchos mazos. Textos prácticos, algo largos, para no quedarse en el eslogan.",
		tags: ["habitos", "estudio", "seed", "productividad"],
		cover: COVER.olive,
		createdDaysAgo: 20,
		updatedDaysAgo: 1,
		studiedDaysAgo: 0,
		studiedHoursAgo: 20,
		cards: [
			{
				front: "¿Qué diferencia hay entre releer un mazo y recuperarlo?",
				back:
					"Releer produce familiaridad: el texto parece conocido en cuanto aparece. Recuperar obliga a reconstruir la respuesta antes de verla, y ese esfuerzo es el que fortalece el recuerdo. Un mazo se estudia mal si la tarjeta se da la vuelta al primer titubeo. Conviene escribir o decir el reverso, aunque sea incompleto, y solo después comparar.",
			},
			{
				front: "¿Cómo se reparte una sesión cuando la lista de mazos ya no cabe en la pantalla?",
				back:
					"No hace falta abrirlos todos. Se elige un subconjunto por etiqueta o por fecha de último estudio, se fija un tope de tarjetas y se deja el resto para otro día. La pantalla desbordada es una razón para filtrar, no para estudiar en el orden en que Mongo devolvió los documentos.",
			},
		],
	},
	{
		key: "roma",
		name: "Historia de Roma",
		description:
			"República, imperio y vida cotidiana. Sirve también como cola larga de la lista de mazos: no es un caso especial de interfaz, solo volumen.",
		tags: ["historia", "roma", "seed"],
		cover: COVER.rust,
		createdDaysAgo: 33,
		updatedDaysAgo: 8,
		studiedDaysAgo: 15,
		cards: [
			{
				front: "¿Qué problema político resuelve mal la República al crecer fuera de Italia?",
				back:
					"Sigue siendo un gobierno de magistrados anuales pensado para una ciudad, mientras el ejército y las provincias crean mandos largos, botín y clientelas. El Senado no integra a los aliados itálicos ni a las élites provinciales al mismo ritmo que usa sus recursos. De esa tensión salen las guerras civiles: el poder personal se vuelve la forma de hacer lo que las instituciones anuales ya no alcanzan.",
			},
			{
				front: "¿Por qué el principado de Augusto insiste en parecer una restauración?",
				back:
					"Porque la palabra rey seguía siendo inaceptable y la guerra civil había agotado la ciudad. Augusto acumula potestades republicanas —imperium, tribunado, prestigio religioso— sin crear un título único que diga «monarca». El régimen es nuevo en la concentración del poder y viejo en el vocabulario. Memorizar solo «fue emperador» borra precisamente el cuidado con el que evitó decirlo.",
			},
		],
	},
	{
		key: "celula",
		name: "Biología celular",
		description:
			"Orgánulos y membrana, explicados con suficiente detalle para que el reverso no sea una lista de nombres sueltos.",
		tags: ["biologia", "celula", "seed", "ciencia"],
		cover: COVER.pine,
		createdDaysAgo: 28,
		updatedDaysAgo: 7,
		cards: [
			{
				front: "¿Qué problema resuelve la membrana y qué costo impone?",
				back:
					"Separa un medio interno donde las reacciones pueden mantener concentraciones distintas a las del exterior. El costo es que casi nada entra o sale por su cuenta: hacen falta proteínas de canal y de transporte, y eso consume energía cuando el movimiento va contra gradiente. Una célula no es una bolsa pasiva. Es un interior regulado que paga, en ATP, el derecho a ser distinto del agua que la rodea.",
			},
			{
				front: "¿Por qué las mitocondrias no son «la batería» en sentido literal?",
				back:
					"No almacenan una reserva grande de energía como un objeto cargado. Transfieren la energía de los alimentos a ATP de forma continua, y el ATP se usa en segundos. Si se detiene la cadena, la célula no vive de una carga previa: se queda sin moneda energética casi enseguida. La metáfora de la batería ayuda a localizarlas y estorba para entender el flujo.",
			},
		],
	},
	{
		key: "economia",
		name: "Economía",
		description:
			"Incentivos, coste de oportunidad y dinero. Descripciones largas para la búsqueda por texto y para la ficha del mazo.",
		tags: ["economia", "seed", "conceptos"],
		cover: COVER.navy,
		createdDaysAgo: 26,
		updatedDaysAgo: 9,
		studiedDaysAgo: 11,
		cards: [
			{
				front: "¿Qué es el coste de oportunidad cuando ya se pagó la matrícula del curso?",
				back:
					"No es el dinero ya gastado, que es un coste hundido. Es lo que dejas de hacer con la siguiente hora: otra asignatura, dormir, trabajar. La matrícula empuja a «aprovecharla» aunque la hora rindiera más en otro sitio. Decidir con coste de oportunidad mira hacia adelante; decidir para no sentir que la matrícula fue un desperdicio mira hacia atrás.",
			},
			{
				front: "¿Qué hace el precio además de cobrar?",
				back:
					"Resume información dispersa: escasez, preferencias y costes que nadie tiene completos. Cuando sube, pide a quien consume que se retire si valora menos el bien, y a quien produce que ofrezca más. Fijarlo por decreto sin cambiar la cantidad disponible no crea el bien; redistribuye la escasez en colas, cupos o mercado negro.",
			},
		],
	},
	{
		key: "ingles",
		name: "Inglés: matices",
		description:
			"Pares de palabras que un diccionario de una línea aplana. Los ejemplos son frases completas para ver texto largo en anverso y reverso.",
		tags: ["ingles", "idiomas", "seed", "vocabulario"],
		cover: COVER.forest,
		createdDaysAgo: 22,
		updatedDaysAgo: 2,
		studiedDaysAgo: 3,
		cards: [
			{
				front: "¿En qué no coinciden «say», «tell» y «speak» aunque los tres se traduzcan cerca de «decir»?",
				back:
					"Say introduce las palabras: she said that the deck was empty. Tell exige un destinatario: she told me the deck was empty. Speak habla de la actividad o del idioma: she spoke for an hour, she speaks Spanish. Mezclarlos suena a traducción palabra a palabra. En una tarjeta de estudio, la frase completa enseña más que la tríada de infinitivos.",
			},
			{
				front: "¿Cuándo «actually» no significa «actualmente»?",
				back:
					"Casi siempre. Actually corrige o precisa: I actually meant the empty deck, not the long one. Actualmente es currently o nowadays. El falso amigo es breve en el diccionario y largo en la conversación, porque cambia el sentido de toda la frase y no solo el del adverbio.",
			},
		],
	},
	{
		key: "renacimiento",
		name: "Arte del Renacimiento",
		description:
			"Perspectiva, patronazgo y un par de obras. Mazo de relleno culto para alargar la lista sin repetir el mismo tema.",
		tags: ["arte", "renacimiento", "seed", "cultura"],
		cover: COVER.terracotta,
		createdDaysAgo: 18,
		updatedDaysAgo: 10,
		cards: [
			{
				front: "¿Qué cambia la perspectiva lineal en el cuadro además de «que se vea hondo»?",
				back:
					"Organiza el espacio con un punto de fuga y un espectador supuesto. El cuadro deja de ser una superficie de símbolos del mismo tamaño y pasa a ser una ventana con una posición desde la que se mira. Eso afecta a la arquitectura pintada, a la escala de las figuras y al lugar del donante. No es un truco óptico suelto: es una teoría de dónde está quien contempla.",
			},
			{
				front: "¿Por qué el patronazgo no es un detalle biográfico menor?",
				back:
					"Quien paga encarga el tema, el tamaño y a menudo el lugar donde se verá la obra. Un retablo, un fresco de cámara y una estatua de plaza no responden a la misma «inspiración». Estudiar el Renacimiento solo como genios sueltos esconde que muchas decisiones formales son respuestas a un contrato.",
			},
		],
	},
	{
		key: "discreta",
		name: "Matemáticas discretas",
		description:
			"Grafos, inducción y conteo. Reversos escritos para leerlos despacio, no para reconocer una fórmula al vuelo.",
		tags: ["matematicas", "discreta", "seed"],
		cover: COVER.ink,
		createdDaysAgo: 16,
		updatedDaysAgo: 4,
		studiedDaysAgo: 7,
		cards: [
			{
				front: "¿Qué demuestra una inducción y en qué paso se suele colar el error?",
				back:
					"Demuestra que una propiedad vale para todos los enteros a partir de una base, si al asumirla para k se sigue para k+1. El error frecuente es que el paso inductivo usa algo que solo es cierto para k grande, o que la base no es el primer caso donde la propiedad importa. Comprobar n = 1 y saltar a «y así sucesivamente» no es una prueba: es la parte que la inducción obliga a escribir.",
			},
			{
				front: "¿Qué informa el grado de un vértice que la simple cantidad de nodos no dice?",
				back:
					"Cuántas aristas lo tocan. Dos grafos con los mismos vértices pueden ser incomunicables o estar completamente conectados. La suma de los grados es el doble del número de aristas, así que un dibujo donde todos los vértices tienen grado impar y el recuento no cuadra está mal contado antes de estar mal dibujado.",
			},
		],
	},
	{
		key: "cocina",
		name: "Cocina de fuego lento",
		description:
			"Técnica, no recetas enteras. Está para que la lista de mazos siga por debajo del pliegue y el buscador tenga otra palabra distinta.",
		tags: ["cocina", "seed", "practica"],
		cover: COVER.rust,
		createdDaysAgo: 14,
		updatedDaysAgo: 14,
		cards: [
			{
				front: "¿Qué se gana al cocinar a fuego lento un corte con mucho colágeno?",
				back:
					"Tiempo y temperatura moderada convierten el colágeno en gelatina y el músculo deja de estar seco y duro. Subir el fuego para «ir más rápido» contrae las fibras y expulsa el agua antes de que esa conversión ocurra. El reverso largo importa porque la tarjeta no pide el nombre de un guiso: pide el mecanismo, que es lo que se puede transferir a otra olla.",
			},
		],
	},
	{
		key: "astronomia",
		name: "Astronomía",
		description:
			"Escalas y luz. Un mazo corto, con un reverso deliberadamente extenso, para mezclar contadores bajos con texto largo.",
		tags: ["astronomia", "ciencia", "seed"],
		cover: COVER.navy,
		createdDaysAgo: 13,
		updatedDaysAgo: 3,
		studiedDaysAgo: 13,
		cards: [
			{
				front: "¿Por qué mirar lejos es mirar atrás, y qué límite pone eso a «ver el presente» del universo?",
				back:
					"La luz tarda. La del Sol tiene unos ocho minutos; la de una galaxia lejana puede haber salido cuando en la Tierra aún no había estrellas de la generación del Sol. No existe una fotografía simultánea del cosmos. Cada instrumento mezcla épocas distintas según la distancia. Por eso un mapa del cielo profundo es también un corte temporal, y pedir «el universo ahora» como si fuera un paisaje contemporáneo mezcla lugares cuyo ahora no nos ha llegado.",
			},
		],
	},
	{
		key: "constitucion",
		name: "Derecho constitucional",
		description:
			"Instituciones y límites del poder, en párrafos. El nombre es normal; el texto de las cartas no, para que el desborde esté en el contenido y no solo en la lista.",
		tags: ["derecho", "constitucion", "seed"],
		cover: COVER.slate,
		createdDaysAgo: 11,
		updatedDaysAgo: 5,
		cards: [
			{
				front: "¿Qué separa un derecho fundamental de una política que el legislador podría cambiar el año que viene?",
				back:
					"El primero condiciona al legislador: una ley que lo vacía puede ser invalidada, y su reforma suele exigir un procedimiento más duro que el de una ley ordinaria. Una política de gasto o de organización administrativa, en cambio, vive en la mayoría de turno. Confundirlos hace que cualquier preferencia se presente como derecho y que cualquier derecho se trate como ocurrencia del gobierno actual. La tarjeta pide esa diferencia, no la lista completa del articulado.",
			},
			{
				front: "¿Para qué sirve dividir funciones si al final todas emanan de la misma constitución?",
				back:
					"Para que quien hace la ley no sea, en el mismo acto, quien la aplica al caso concreto ni quien juzga el conflicto. La unidad del orden jurídico no exige una sola mano. Exige que las manos distintas se deban obediencia a reglas distintas de nombramiento, de tiempo en el cargo y de control. Sin eso, la constitución sería un prólogo de una sola institución.",
			},
		],
	},
	{
		key: "musica",
		name: "Música: forma y escucha",
		description:
			"Forma sonata, motivo y silencio. Otro mazo de la cola, con portada distinta para que el scroll se lea también por el color.",
		tags: ["musica", "seed", "cultura"],
		cover: COVER.plum,
		createdDaysAgo: 9,
		updatedDaysAgo: 9,
		cards: [
			{
				front: "¿Qué se escucha en una forma sonata además de «una melodía que vuelve»?",
				back:
					"Una exposición que presenta materiales en tensión de tonalidad, un desarrollo que los fragmenta y una reexposición que los devuelve reconciliados en la tónica. La melodía que vuelve importa, pero importa dentro de ese recorrido armónico. Quien solo espera el estribillo oye repetición; quien sigue la tonalidad oye una discusión que se cierra.",
			},
		],
	},
	{
		key: "europa",
		name: "Geografía de Europa",
		description:
			"Ríos, climas y por qué el mapa político no coincide con el físico. Incluido para alargar la pantalla de mazos.",
		tags: ["geografia", "europa", "seed"],
		cover: COVER.forest,
		createdDaysAgo: 8,
		updatedDaysAgo: 6,
		cards: [
			{
				front: "¿Por qué el Danubio organiza mejor la historia de una franja de Europa que muchas fronteras actuales?",
				back:
					"Atraviesa o roza países que hoy se estudian por separado y durante siglos fue vía de comercio, de imperio y de límite. Las fronteras del siglo XX cortan esa continuidad. Memorizar capitales sin el río produce una lista; seguir el río produce un corredor. Esta tarjeta está escrita larga para que el reverso no quepa en el hueco de una definición breve.",
			},
		],
	},
	{
		key: "mitologia",
		name: "Mitología griega",
		description:
			"Mitos como relatos con función, no como cromos de dioses. Suma otra ficha al scroll.",
		tags: ["mitologia", "grecia", "seed", "cultura"],
		cover: COVER.clay,
		createdDaysAgo: 7,
		updatedDaysAgo: 7,
		cards: [
			{
				front: "¿Qué cuenta el mito de Prometeo si no se reduce a «robó el fuego»?",
				back:
					"Cuenta un traspaso de técnica de los dioses a los humanos y el castigo de quien media ese traspaso. El fuego es cocina, metal y sacrificio, no solo llama. Según la versión, Prometeo engaña a Zeus en el reparto de la víctima y deja a los mortales la parte comestible. Leer solo el robo heroico pierde el conflicto sobre quién administra el conocimiento práctico.",
			},
		],
	},
	{
		key: "quimica",
		name: "Química orgánica",
		description:
			"Grupos funcionales y la idea de que la forma de la molécula es el comportamiento. Mazo breve al final de la lista.",
		tags: ["quimica", "organica", "seed", "ciencia"],
		cover: COVER.teal,
		createdDaysAgo: 5,
		updatedDaysAgo: 2,
		studiedDaysAgo: 5,
		cards: [
			{
				front: "¿Por qué dos moléculas con la misma fórmula pueden oler y reaccionar distinto?",
				back:
					"Porque la fórmula cuenta átomos y no cuenta cómo están unidos ni en qué espacio. Isómeros de cadena, de posición o geométricos exponen grupos distintos al entorno. El olor y la reactividad dependen de esa superficie, de los enlaces polares y de si un reactivo puede acercarse. Aprender solo C₄H₁₀ no distingue el butano del metilpropano.",
			},
		],
	},
	{
		key: "literatura",
		name: "Literatura latinoamericana",
		description:
			"Narrativa del siglo XX, con contexto y no solo títulos. Cierra la lista para que haya que hacer scroll de verdad.",
		tags: ["literatura", "latinoamerica", "seed"],
		cover: COVER.wine,
		createdDaysAgo: 4,
		updatedDaysAgo: 1,
		studiedDaysAgo: 1,
		studiedHoursAgo: 8,
		cards: [
			{
				front: "¿Qué hace el realismo mágico con el hecho extraordinario dentro del relato?",
				back:
					"Lo enuncia con el mismo tono que la cosecha o el parte de defunción. No pide al lector que se asombre mediante signos de exclamación ni que explique el prodigio por un sueño. El efecto depende de esa naturalidad: si el narrador subraya lo imposible, el procedimiento se deshace y queda fantasía señalada. Por eso no basta con recordar «hay cosas raras»; hay que recordar quién las cuenta como si no lo fueran.",
			},
			{
				front: "¿Por qué situar una novela en un pueblo aislado no es solo decorado?",
				back:
					"Concentra el poder, el rumor y la familia en un espacio donde no hay otra instancia que los corrija. El pueblo funciona como laboratorio político: lo que en una capital se diluye en instituciones, aquí se ve en una casa. Macondo y Comala no son postales. Son lugares donde la historia oficial y la memoria doméstica se pisan.",
			},
		],
	},
	{
		key: "logica",
		name: "Lógica informal",
		description:
			"Falacias contadas con un ejemplo desarrollado. Penúltimo empujón para que la lista no quepa.",
		tags: ["logica", "seed", "teorias"],
		cover: COVER.olive,
		createdDaysAgo: 3,
		updatedDaysAgo: 3,
		cards: [
			{
				front: "¿En qué falla un argumento ad hominem aunque la persona criticada merezca la crítica?",
				back:
					"Falla porque la conclusión sobre el asunto no se sigue del defecto de quien habla. Alguien puede ser arrogante y tener razón sobre la fecha de un tratado, o ser admirable y equivocarse en el cálculo. Señalar el defecto puede ser relevante para fiarse de un testimonio; no sustituye la razón que faltaba. La tarjeta separa «esta persona es mala fuente» de «esta frase es falsa».",
			},
		],
	},
	{
		key: "caligrafia",
		name: "Caligrafía y lectura lenta",
		description:
			"Último mazo de la cola. Una sola carta, texto largo, para variar el contador respecto a los mazos de dos y al mazo vacío.",
		tags: ["caligrafia", "seed", "practica"],
		cover: COVER.clay,
		createdDaysAgo: 2,
		updatedDaysAgo: 2,
		cards: [
			{
				front: "¿Qué entrena copiar un párrafo a mano que no entrena volver a leerlo en pantalla?",
				back:
					"Obliga a decidir cada letra y a sostener la atención en una unidad más pequeña que el renglón. Los errores se notan en el momento, no al terminar el capítulo. No hace falta romantizar la pluma: el mismo efecto aparece al transcribir despacio. En este seed la carta existe para tener un reverso largo dentro de un mazo de una sola ficha, y para que el contador de la lista no sea el mismo número en todos los sitios.",
			},
		],
	},
];

const SHARED_FRONT = "Carta compartida: ¿por qué el repaso espaciado gana al estudio masivo?";
const SHARED_BACK =
	"Esta tarjeta está enlazada a muchos mazos a la vez, vía deckIds, para probar que una carta no pertenece a un solo sitio. El contenido, además, es el método: estudiar todo la víspera crea familiaridad frágil; recuperar la misma idea en días distintos, cuando ya empezaba a olvidarse, fortalece más el recuerdo por el mismo tiempo total. En la interfaz debería contarse en cada mazo enlazado y no duplicarse como documentos distintos. Si se borra uno de esos mazos, la carta tiene que seguir en los demás.";
const SHARED_DECK_KEYS = [
	"estoica",
	"programacion",
	"psicologia",
	"neuro",
	"habitos",
	"roma",
	"economia",
	"ingles",
	"discreta",
	"astronomia",
	"constitucion",
	"literatura",
	"logica",
	"caligrafia",
];

function createDeck(def) {
	const id = new ObjectId();
	const createdAt = daysAgo(def.createdDaysAgo);
	const updatedAt = daysAgo(def.updatedDaysAgo);
	const doc = {
		_id: id,
		name: def.name,
		description: def.description,
		tags: def.tags,
		cover: def.cover,
		createdAt,
		updatedAt,
	};
	if (def.studiedDaysAgo != null) {
		doc.lastTimeStudiedAt = daysAgo(def.studiedDaysAgo, def.studiedHoursAgo ?? 0);
	}
	return { key: def.key, idHex: id.toString(), doc, cards: def.cards };
}

db.decks.deleteMany({});
db.flashcards.deleteMany({});

const deckRecords = DECK_DEFS.map(createDeck);
const deckIdByKey = Object.fromEntries(deckRecords.map((record) => [record.key, record.idHex]));

db.decks.insertMany(deckRecords.map((record) => record.doc));

const flashcards = deckRecords.flatMap((record) =>
	record.cards.map((card) => ({
		_id: new ObjectId(),
		deckIds: [record.idHex],
		tags: card.tags ?? record.doc.tags,
		front: card.front,
		back: card.back,
		createdAt: record.doc.createdAt,
		updatedAt: record.doc.updatedAt,
	})),
);

const sharedDeckIds = SHARED_DECK_KEYS.map((key) => {
	const id = deckIdByKey[key];
	if (!id) {
		throw new Error(`Unknown deck key in shared card: ${key}`);
	}
	return id;
});

flashcards.push({
	_id: new ObjectId(),
	deckIds: sharedDeckIds,
	tags: ["repaso", "memoria", "compartida", "seed", "metodo"],
	front: SHARED_FRONT,
	back: SHARED_BACK,
	createdAt: daysAgo(1),
	updatedAt: now,
});

db.flashcards.insertMany(flashcards);

print("Seed complete.");
print(`Decks: ${deckRecords.length}`);
deckRecords.forEach((record) => {
	const ownCards = record.cards.length;
	const shared = SHARED_DECK_KEYS.includes(record.key) ? 1 : 0;
	print(`  ${record.doc.name.slice(0, 72)} → ${record.idHex} (${ownCards + shared} cartas)`);
});
print(`Flashcards: ${flashcards.length} (1 compartida en ${sharedDeckIds.length} mazos)`);
print(`Mazo vacío → ${deckIdByKey.vacio}`);
print(`Mazo con muchas etiquetas → ${deckIdByKey.etiquetas}`);
print(`Mazo de nombre largo → ${deckIdByKey["nombre-largo"]}`);
print("Example: GET /api/v1/decks");
