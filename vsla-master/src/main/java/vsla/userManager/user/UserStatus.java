package vsla.userManager.user;

public enum UserStatus {
    ACTIVE("Active", "The user account is active and in good standing."),
    PENDING_APPROVAL("Pending Approval", "The user account is pending administrative approval."),
    SUSPENDED("Suspended", "The user account is temporarily suspended."),
    BANNED("Banned", "The user account is permanently banned.");

    private final String displayName;
    private final String description;

    UserStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
