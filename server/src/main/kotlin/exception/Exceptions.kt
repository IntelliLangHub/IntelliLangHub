package exception

class EntityNotFoundException(message: String) : Exception(message)

class CommitNotActiveException(message: String) : Exception(message)