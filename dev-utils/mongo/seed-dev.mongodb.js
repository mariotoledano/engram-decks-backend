// Local development seed only. Wipes decks and flashcards in engram-decks.
// Run from repo root:
//   mongosh "mongodb://localhost:27017/engram-decks" --file dev-utils/mongo/seed-dev.mongodb.js
// Compass: use engram-decks, open Mongosh, paste this entire file and run.

const now = new Date();
const ts = { createdAt: now, updatedAt: now };
const DEFAULT_COVER = { type: "COLOR", value: "#C4622D" };

function createDeck(name, description, tags) {
	const id = new ObjectId();
	return {
		idHex: id.toString(),
		doc: { _id: id, name, description, tags, cover: DEFAULT_COVER, ...ts },
	};
}

function createCard(deckIdHex, front, back, tags) {
	return { _id: new ObjectId(), deckIds: [deckIdHex], tags, front, back, ...ts };
}

function cardsFromPairs(deckIdHex, pairs, tags) {
	return pairs.map(([front, back]) => createCard(deckIdHex, front, back, tags));
}

db.decks.deleteMany({});
db.flashcards.deleteMany({});

const DECK_DEFS = [
	{
		name: "Filosofía estoica",
		description:
			"Marco Aurelio, Epicteto y Séneca: virtudes, dicotomía del control, pasiones y práctica cotidiana.",
		tags: ["filosofia", "estoicismo", "seed"],
		pairs: [
			["¿Qué es la dicotomía del control?", "Separar lo que depende de nosotros (juicio, voluntad) de lo que no (cuerpo, fama, suerte)."],
			["¿Quién escribió las Meditaciones?", "Marco Aurelio, emperador romano; diario personal de ejercicios estoicos."],
			["¿Quién fue Epicteto?", "Filósofo esclavo liberado; enseñanza en Enquiridión y Discursos (Arriano)."],
			["¿Quién fue Séneca?", "Filósofo estoico romano; cartas morales, tragedias y consejos prácticos a Lucilio."],
			["¿Qué es el logos?", "Razón ordenadora del cosmos; vivir conforme a la naturaleza racional."],
			["¿Qué son las cuatro virtudes cardinales (estoicismo)?", "Sabiduría, justicia, coraje (fortaleza) y templanza (moderación)."],
			["¿Qué es un «preferido indiferente»?", "Salud, riqueza, honor: preferibles pero no buenos en sí; no definen la virtud."],
			["¿Qué es un «rechazado indiferente»?", "Enfermedad, pobreza: desagradables pero no malos en sí si actuas con virtud."],
			["¿Qué es apatheia?", "Tranquilidad ante perturbaciones; no insensibilidad, sino ausencia de pasiones destructivas."],
			["¿Qué es una pasión (pathos) en estoicismo?", "Juicio erróneo excesivo: miedo, codicia, ira; se corrige con razón."],
			["¿Qué significa amor fati?", "Amar el destino; aceptar lo ocurrido y usarlo como material de virtud."],
			["¿Qué es memento mori?", "Recordar la mortalidad para priorizar lo esencial y actuar con urgencia moral."],
			["«Obstáculo como camino»", "Quote de Marco Aurelio: el impedimento avanza la acción; lo que bloquea también guía."],
			["¿Qué es la provisión anticipada (praemeditatio malorum)?", "Visualizar adversidades posibles para no sorprenderse y responder mejor."],
			["¿Qué es el «juicio» en Epicteto?", "No las cosas nos perturban, sino nuestros juicios sobre las cosas."],
			["¿Qué es la ciudadanía cosmopolita?", "Somos ciudadanos del mundo (cosmopolis), no solo de una polis."],
			["¿Qué enseña el dicho «quien puede impedirte?»", "Solo tú puedes consentir que algo te dañe moralmente; libertad interior."],
			["¿Qué es vivir «conforme a la naturaleza»?", "Razonar bien, cumplir rol social y aceptar el fluir del universo."],
			["¿Cómo describe Séneca la ira?", "Emoción breve de locura; anticiparla y retrasar la reacción la debilita."],
			["¿Qué papel tiene la reseña nocturna?", "Examinar el día: acciones, fallos, cómo mejorar mañana (costumbre estoica)."],
			["¿Qué es el «presente» para Marco Aurelio?", "El único tiempo que posees; pasado y futuro no son tuyos para vivir."],
			["¿Qué significa «el otro tiene su propio logos»?", "Entender motivaciones ajenas reduce resentimiento; justicia y paciencia."],
			["¿Qué es el prosoché?", "Atención vigilante al momento presente y a tus impresiones (asentimientos)."],
			["«Solo la filosofía es terapia del alma» (Epicteto)", "La razón corrige creencias; la escuela como clínica del carácter."],
			["¿Qué diferencia hay entre placer y bien (estoicismo)?", "El bien es virtud; el placer es indiferente y puede desviar del recto."],
			["¿Qué es la oikeiosis?", "Apropiación: cuidado natural por uno mismo que se extiende a otros racionales."],
			["¿Qué dice Séneca sobre el tiempo?", "No nos dan poco tiempo, sino que desperdiciamos mucho."],
			["¿Qué es una impresión (phantasia)?", "Aparición mental; el sabio no asiente a las falsas sin examinarlas."],
			["¿Cómo se entrena el desapego a la opinión ajena?", "Distinguir lo opinable de lo que importa moralmente; foco en virtud."],
			["¿Qué es el «arte de vivir» estoico?", "Ética como oficio: prácticas diarias, no solo teoría abstracta."],
			["Zeno de Citio", "Fundador del estoicismo en Atenas (~300 a.C.); influencia cínica y megárica."],
			["Crisipo", "Tercer jefe de la Stoa; lógica, física y ética sistematizadas; gran influencia helenística."],
		],
	},
	{
		name: "Programación",
		description:
			"Fundamentos de software: complejidad, diseño, control de versiones, HTTP, pruebas y buenas prácticas.",
		tags: ["programacion", "software", "seed"],
		pairs: [
			["¿Qué es la notación O grande?", "Cota asintótica del crecimiento de tiempo o espacio; ignora constantes."],
			["¿Qué es O(log n)?", "Crecimiento logarítmico; típico en búsqueda binaria en datos ordenados."],
			["¿Qué es O(n)?", "Lineal; recorrer una lista de tamaño n una vez."],
			["Principio DRY", "Don't Repeat Yourself: una sola fuente de verdad para cada pieza de conocimiento."],
			["Principio KISS", "Keep It Simple, Stupid: la solución más simple que funcione suele ser mejor."],
			["¿Qué es un algoritmo greedy?", "Elige la opción localmente óptima en cada paso; no siempre es globalmente óptimo."],
			["¿Qué es recursión?", "Función que se llama a sí misma; requiere caso base para terminar."],
			["¿Qué es la pila de llamadas?", "Estructura que guarda marcos de funciones activas; overflow si recursión infinita."],
			["¿Qué es un hash map?", "Estructura clave-valor con acceso amortizado O(1) en hash uniforme."],
			["SOLID — S (Single Responsibility)", "Una clase/módulo debería tener una sola razón para cambiar."],
			["SOLID — O (Open/Closed)", "Abierto a extensión, cerrado a modificación."],
			["SOLID — L (Liskov Substitution)", "Subtipos deben poder sustituir a sus bases sin romper el contrato."],
			["SOLID — I (Interface Segregation)", "Interfaces pequeñas y específicas mejor que una gigante."],
			["SOLID — D (Dependency Inversion)", "Depender de abstracciones, no de implementaciones concretas."],
			["¿Qué es un commit en Git?", "Instantánea del repositorio con mensaje; historial inmutable en DAG."],
			["¿Qué es una rama (branch)?", "Puntero móvil a un commit; permite trabajo paralelo e integración."],
			["¿Qué es un merge conflict?", "Git no puede fusionar cambios en la misma línea sin intervención humana."],
			["GET vs POST (HTTP)", "GET: idempotente, lectura; POST: crear o acciones no idempotentes."],
			["PUT vs PATCH", "PUT: reemplazo completo del recurso; PATCH: actualización parcial."],
			["Código 201 Created", "Recurso creado con éxito; suele incluir Location del nuevo recurso."],
			["Código 404 Not Found", "Recurso no existe en el servidor para esa URL."],
			["Código 422 / 400 validación", "Cuerpo mal formado o reglas de negocio/validación incumplidas."],
			["¿Qué es REST?", "Estilo arquitectónico: recursos con URIs, verbos HTTP, representaciones (JSON)."],
			["¿Qué es un test unitario?", "Prueba una unidad aislada (función/clase) con dependencias simuladas."],
			["¿Qué es un mock?", "Doble de test que verifica interacciones y devuelve comportamiento programado."],
			["¿Qué es inmutabilidad útil en código?", "Estados que no cambian tras creación simplifican razonamiento y concurrencia."],
			["¿Qué es una condición de carrera?", "Resultado depende del orden de hilos/procesos sin sincronización."],
			["¿Qué es un deadlock?", "Procesos se bloquean mutuamente esperando recursos que el otro tiene."],
			["¿Qué es la inyección de dependencias?", "Recibir dependencias desde fuera en lugar de crearlas dentro."],
			["¿Qué es SQL vs NoSQL (idea)?", "SQL: tablas y esquema; NoSQL: modelos documento/clave-valor más flexibles."],
			["¿Qué es un índice en base de datos?", "Estructura auxiliar que acelera búsquedas a costa de escritura y espacio."],
			["¿Qué es refactoring?", "Cambiar estructura interna sin alterar comportamiento observable externo."],
		],
	},
	{
		name: "Psicología",
		description:
			"Cognición, aprendizaje, motivación, teorías clásicas y sesgos que influyen en el juicio.",
		tags: ["psicologia", "ciencia", "seed"],
		pairs: [
			["¿Qué estudia la psicología cognitiva?", "Procesos mentales: percepción, memoria, atención, lenguaje, razonamiento."],
			["¿Qué es el condicionamiento clásico?", "Pavlov: asociación estímulo neutral → estímulo incondicionado → respuesta condicionada."],
			["¿Qué es el condicionamiento operante?", "Skinner: consecuencias (refuerzo/castigo) moldean la frecuencia conductual."],
			["¿Qué es el refuerzo positivo?", "Añadir estímulo deseado tras conducta para aumentar su probabilidad."],
			["¿Qué es el refuerzo negativo?", "Quitar estímulo aversivo tras conducta para aumentar su probabilidad."],
			["Etapas de Piaget — sensomotor", "0-2 años: coordinación sensoriomotriz; objeto permanente."],
			["Etapas de Piaget — preoperacional", "2-7: simbolismo, egocentrismo; lenguaje y juego simbólico."],
			["Etapas de Piaget — operaciones concretas", "7-11: lógica sobre lo tangible; conservación, reversibilidad."],
			["Etapas de Piaget — operaciones formales", "11+: pensamiento abstracto e hipotético-deductivo."],
			["¿Qué es el inconsciente (Freud)?", "Procesos mentales no accesibles directamente; deseos y conflictos reprimidos."],
			["Id, ego y superego (idea freudiana)", "Id: pulsiones; ego: realidad; superyó: normas y culpa."],
			["¿Qué es la memoria a corto plazo / trabajo?", "Retención breve y capacidad limitada; manipulación activa (working memory)."],
			["¿Qué es la memoria a largo plazo?", "Almacenamiento duradero; declarativa (episódica/semántica) y procedural."],
			["¿Qué es el efecto de primacía?", "Recordamos mejor los primeros ítems de una lista (serial position)."],
			["¿Qué es el efecto de recencia?", "Recordamos mejor los últimos ítems, sobre todo con intervalo de prueba."],
			["¿Qué es la curva del olvido (Ebbinghaus)?", "La retención decae con el tiempo; repaso espaciado la mitiga."],
			["¿Qué es el sesgo de confirmación?", "Buscar y valorar información que confirma creencias previas."],
			["¿Qué es el sesgo de anclaje?", "Influencia desproporcionada del primer número o dato recibido."],
			["¿Qué es la heurística de disponibilidad?", "Juzgar probabilidad por facilidad de recordar ejemplos."],
			["¿Qué es la heurística de representatividad?", "Juzgar por parecido a prototipo, ignorando bases rate."],
			["Jerarquía de necesidades de Maslow (idea)", "Pirámide de motivación: fisiológicas → seguridad → pertenencia → estima → autorrealización."],
			["Teoría de la autodeterminación — autonomía", "Necesidad de sentir elección y congruencia con valores propios."],
			["Teoría de la autodeterminación — competencia", "Necesidad de eficacia y dominio progresivo."],
			["Teoría de la autodeterminación — relación", "Necesidad de vincularse y pertenecer."],
			["¿Qué es la disonancia cognitiva?", "Malestar por actitudes y conductas en conflicto; tendencia a reducir la tensión."],
			["¿Qué es el aprendizaje social (Bandura)?", "Aprender por observación, modelado e imitación."],
			["¿Qué es la atención selectiva?", "Filtrar estímulos relevantes; «cóctel party effect»."],
			["¿Qué es el efecto Stroop?", "Dificultad al nombrar color de palabra que nombra otro color."],
			["¿Qué es la inteligencia fluida vs cristalizada?", "Fluida: razonamiento nuevo; cristalizada: conocimiento acumulado."],
			["¿Qué es mindfulness (psicología clínica)?", "Atención plena al presente sin juicio; usado en reducción de estrés (MBSR)."],
			["¿Qué es un trastorno del estado de ánimo?", "Alteración duradera del ánimo (p. ej. depresión mayor, bipolar)."],
			["¿Qué es la resiliencia psicológica?", "Capacidad de adaptarse favorablemente ante adversidad y estrés."],
		],
	},
	{
		name: "Neurociencia",
		description:
			"Cerebro, neuronas, sistemas sensoriales, plasticidad, memoria y métodos de estudio.",
		tags: ["neurociencia", "cerebro", "seed"],
		pairs: [
			["¿Qué es una neurona?", "Célula nerviosa: soma, dendritas (entrada), axón (salida) y sinapsis."],
			["¿Qué es el potencial de acción?", "Impulso eléctrico breve que viaja por el axón por apertura de canales iónicos."],
			["¿Qué es una sinapsis?", "Unión entre neuronas donde un neurotransmisor transmite la señal."],
			["¿Qué hace la mielina?", "Aísla el axón y acelera la conducción (saltatoria en nódulos de Ranvier)."],
			["¿Qué es la corteza cerebral?", "Capa externa del cerebro; procesamiento sensorial, motor y cognitivo superior."],
			["¿Qué hace el hipocampo?", "Crucial para formación de memorias episódicas declarativas y navegación espacial."],
			["¿Qué hace la amígdala?", "Procesamiento emocional, especialmente miedo y aprendizaje emocional."],
			["¿Qué hace el cerebelo?", "Coordinación motora, equilibrio y ciertos aspectos de aprendizaje motor."],
			["¿Qué hace el tálamo?", "Relé sensorial hacia la corteza (excepto olfato)."],
			["¿Qué es el tronco encefálico?", "Respiración, ritmo cardíaco, arousal; conexión cerebro-médula."],
			["¿Qué son los lóbulos frontales?", "Planificación, control ejecutivo, personalidad y mucha cognición social."],
			["¿Qué son los lóbulos temporales?", "Audición, lenguaje (área de Wernicke), memoria (hipocampo medial)."],
			["¿Qué son los lóbulos parietales?", "Integración sensorial somatosensorial y espacio corporal."],
			["¿Qué son los lóbulos occipitales?", "Procesamiento visual primario y asociativo."],
			["¿Qué es la plasticidad sináptica?", "Cambio en fuerza de conexiones; base del aprendizaje."],
			["¿Qué es LTP (potenciación a largo plazo)?", "Aumento duradero de eficacia sináptica tras estimulación repetida."],
			["¿Qué es LTD?", "Depresión a largo plazo sináptica; también moldea redes."],
			["¿Qué es la dopamina (funciones clave)?", "Recompensa, motivación, movimiento (vía nigroestriatal)."],
			["¿Qué es la serotonina?", "Modula ánimo, sueño, apetito; ántero del sistema serotoninérgico."],
			["¿Qué es el GABA?", "Principal neurotransmisor inhibidor en el SNC."],
			["¿Qué es el glutamato?", "Principal neurotransmisor excitatorio en el SNC."],
			["¿Qué es la barrera hematoencefálica?", "Filtra sangre-cerebro; protege pero limita ciertos fármacos."],
			["¿Qué mide el EEG?", "Actividad eléctrica sumada en el cuero cabelludo; buena resolución temporal."],
			["¿Qué mide la fMRI?", "Cambios de oxigenación sanguínea (BOLD); buena resolución espacial."],
			["¿Qué es la neurogénesis adulta?", "Nuevas neuronas sobre todo en giro dentado del hipocampo (limitada en humanos)."],
			["¿Qué es el sistema nervioso simpático?", "«Lucha o huida»: arousal, FC, pupilas dilatadas."],
			["¿Qué es el parasimpático?", "«Descanso y digestión»: recuperación y conservación de energía."],
			["¿Qué es la hemisferio dominante para el lenguaje?", "En la mayoría, hemisferio izquierdo (áreas de Broca y Wernicke)."],
			["¿Qué es el área de Broca?", "Producción del lenguaje; lesión → afasia no fluente."],
			["¿Qué es el área de Wernicke?", "Comprensión del lenguaje; lesión → afasia fluente sin sentido."],
			["¿Qué es el sueño REM?", "Sueño paradójico, movimientos oculares, sueños vívidos; consolidación de memoria."],
			["¿Qué es un lesion study?", "Inferir función de región cerebral por déficit tras daño focal."],
		],
	},
];

const deckRecords = DECK_DEFS.map((def) => {
	const { idHex, doc } = createDeck(def.name, def.description, def.tags);
	return { idHex, doc, pairs: def.pairs, tags: def.tags };
});

const decks = deckRecords.map((r) => r.doc);
db.decks.insertMany(decks);

const flashcards = deckRecords.flatMap((r) => cardsFromPairs(r.idHex, r.pairs, r.tags));
db.flashcards.insertMany(flashcards);

print("Seed complete.");
print(`Decks: ${decks.length}`);
deckRecords.forEach((r) => {
	print(`  ${r.doc.name} → ${r.idHex}`);
});
print(`Flashcards: ${flashcards.length}`);
print("Example: GET /api/v1/decks/<id>/flashcards");
