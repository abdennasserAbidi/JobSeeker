package com.myjob.jobseeker.model;

public enum ToolCategory {

    // Outils électriques
    IDLE("", ""),
    PERCEUSE("Perceuse", "🔩"),
    MEULEUSE("Meuleuse", "⚙️"),
    SCIE_CIRCULAIRE("Scie Circulaire", "🪚"),
    SCIE_SAUTEUSE("Scie Sauteuse", "🔧"),
    PONCEUSE("Ponceuse", "📐"),
    MARTEAU_PIQUEUR("Marteau Piqueur", "⚒️"),
    COMPRESSEUR("Compresseur", "💨"),
    GROUPE_ELECTROGENE("Groupe Électrogène", "⚡"),
    KARCHER("Karcher", "🚿"),
    ASPIRATEUR_PROFESSIONNEL("Aspirateur Pro", "🌪️"),

    // Outils de construction
    BETONNIERE("Bétonnière", "🏗️"),
    ECHAFAUDAGE("Échafaudage", "🪜"),
    ECHELLE("Échelle", "🪜"),
    NIVEAU_LASER("Niveau Laser", "📏"),

    // Outils de jardinage
    TONDEUSE("Tondeuse", "🌱"),
    TAILLE_HAIE("Taille-haie", "✂️"),
    TRONCONNEUSE("Tronçonneuse", "🪓"),
    DEBROUSSAILLEUSE("Débroussailleuse", "🌾"),
    MOTOCULTEUR("Motoculteur", "🚜"),

    // Matériel de levage
    GRUE("Grue", "🏗️"),
    NACELLE("Nacelle", "🚧"),
    DIABLE("Diable", "📦"),
    TRANSPALETTE("Transpalette", "🔄"),

    // Matériel de nettoyage
    NETTOYEUR_VAPEUR("Nettoyeur Vapeur", "💨"),
    AUTOLAVEUSE("Autolaveuse", "🧼"),
    MONOBROSSE("Monobrosse", "⚪"),

    // Matériel de peinture
    PISTOLET_PEINTURE("Pistolet à Peinture", "🎨"),
    ROULEAU_PROFESSIONNEL("Rouleau Pro", "🖌️"),

    // Véhicules utilitaires
    CAMIONNETTE("Camionnette", "🚐"),
    CAMION("Camion", "🚚"),
    FOURGON("Fourgon", "🚙"),
    REMORQUE("Remorque", "🚛"),

    // Matériel événementiel
    TENTE("Tente", "⛺"),
    CHAISES_TABLES("Chaises & Tables", "🪑"),
    SONO("Sonorisation", "🔊"),
    ECLAIRAGE("Éclairage", "💡"),

    // Autre
    AUTRE_OUTIL("Autre Outil", "🔧");

    private final String displayName;
    private final String icon;

    ToolCategory(String displayName, String icon) {
        this.displayName = displayName;
        this.icon = icon;
    }

    public String getDisplayName() { return displayName; }
    public String getIcon() { return icon; }
}
