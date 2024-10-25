# 🚀spring-kafka

> Repository d'exemple d'utilisation de Spring Kafka

## 🎉 Bienvenue

Ce repository a pour but de montrer **simplement** comment produire et consommer (à venir) des messages provenant de la plateforme Confluent Kafka.<br>
Il vous permettra aussi de configurer rapidement votre projet en vous appuyant sur:<br>

> - Le fichier [compose.yaml](compose.yaml)
>   - Permet de monter une infra Confluent Kafka en mode Kraft
>   - Créé automatiquement un topic au démarrage des conteneurs
>   - Configure automatique le schema AVRO au démarrage des conteneur en utilisant une Schema Registry
> - Avoir des exemples de tests d'intégration

## 🍳 Composition du repository

📖 Un premier "module", **schema**, contient une définition de schema AVRO: [README.md](schema/README.md)<br/>
📤 Un second "module", **kafka-producer**, expose une API REST et permet de publier un message sur la plateforme Confluent Kafka 🙂: [README.md](kafka-producer/README.md)

## ✍️ Travaux en cours

- Réalisation d'un consumer 📥
- Réalisation d'une application Spring Kafka Streams entre le producer et le consumer 📬
- Autres idées ❓