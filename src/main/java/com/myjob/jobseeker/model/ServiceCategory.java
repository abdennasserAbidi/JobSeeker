package com.myjob.jobseeker.model;

public enum ServiceCategory {
    IDLE("", ""),
    MENUISIER("Menuisier", "🔨"),
    PLOMBIER("Plombier", "🔧"),
    ELECTRICIEN("Électricien", "💡"),
    PEINTRE("Peintre", "🎨"),
    MACONNERIE("Maçonnerie", "🧱"),
    CARRELEUR("Carreleur", "⬜"),
    PLATRERIE("Plâtrerie", "🏗️"),
    SOUDEUR("Soudeur", "⚡"),
    VITRIER("Vitrier", "🪟"),
    CLIMATISATION("Climatisation", "❄️"),

    // Jardinage & Extérieur
    JARDINAGE("Jardinage", "🌱"),
    PAYSAGISTE("Paysagiste", "🌳"),
    ELAGAGE("Élagage", "🌲"),
    PISCINE("Entretien Piscine", "🏊"),

    // Nettoyage & Entretien
    NETTOYAGE("Nettoyage", "🧹"),
    NETTOYAGE_PROFOND("Nettoyage Profond", "✨"),
    DESINFECTION("Désinfection", "🦠"),
    PRESSING("Pressing", "👔"),

    // Déménagement & Transport
    DEMENAGEMENT("Déménagement", "📦"),
    TRANSPORT("Transport", "🚚"),
    LEVAGE("Levage", "🏗️"),

    // Réparation & Maintenance
    REPARATION_ELECTROMENAGER("Réparation Électroménager", "🔌"),
    REPARATION_INFORMATIQUE("Réparation Informatique", "💻"),
    REPARATION_MOBILE("Réparation Mobile", "📱"),
    SERRURIER("Serrurier", "🔑"),
    COUTURE("Couture", "🧵"),
    CORDONNIER("Cordonnier", "👞"),

    // Automobile
    MECANICIEN("Mécanicien Auto", "🚗"),
    CARROSSERIE("Carrosserie", "🔧"),
    LAVAGE_AUTO("Lavage Auto", "🚿"),
    DEPANNAGE_AUTO("Dépannage Auto", "🆘"),

    // Services à domicile
    FEMME_MENAGE("Femme de Ménage", "🧽"),
    GARDE_ENFANTS("Garde d'Enfants", "👶"),
    COURS_PARTICULIERS("Cours Particuliers", "📚"),
    AIDE_PERSONNES_AGEES("Aide Personnes Âgées", "👴"),
    CUISINE_DOMICILE("Cuisine à Domicile", "👨‍🍳"),

    // Événementiel
    TRAITEUR("Traiteur", "🍽️"),
    PHOTOGRAPHE("Photographe", "📸"),
    VIDEASTE("Vidéaste", "🎥"),
    DJ("DJ", "🎧"),
    ANIMATION("Animation", "🎉"),
    DECORATION("Décoration", "🎀"),

    // Bien-être & Beauté
    COIFFEUR_DOMICILE("Coiffeur à Domicile", "💇"),
    ESTHETICIENNE("Esthéticienne", "💅"),
    MASSAGE("Massage", "💆"),

    // Artisanat
    TAPISSIER("Tapissier", "🛋️"),
    FORGERON("Forgeron", "⚒️"),
    MIROITIER("Miroitier", "🪞"),

    // Location d'outils
    LOCATION_OUTILS("Location d'Outils", "🛠️"),

    // Autre
    AUTRE("Autre", "⚙️");

    private final String displayName;
    private final String icon;

    ServiceCategory(String displayName, String icon) {
        this.displayName = displayName;
        this.icon = icon;
    }

    public String getDisplayName() { return displayName; }
    public String getIcon() { return icon; }
}