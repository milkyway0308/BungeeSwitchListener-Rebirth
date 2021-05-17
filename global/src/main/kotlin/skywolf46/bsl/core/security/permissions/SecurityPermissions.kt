package skywolf46.bsl.core.security.permissions

// TODO add custom permissions
enum class SecurityPermissions(vararg val inner: SecurityPermissions) {
    OPEN_API,
    RESOURCE_GET,
    RESOURCE_SET,
    ADMIN(OPEN_API, RESOURCE_GET, RESOURCE_SET);

    fun hasPermission(perm: SecurityPermissions) = perm == this || inner.contains(perm)

}