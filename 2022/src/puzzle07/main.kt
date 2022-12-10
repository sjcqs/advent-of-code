package puzzle07

import readInput
import requireEquals
import split


private sealed interface Command {
    val name: CommandName

    data class ChangeDirectory(
        val target: Target
    ) : Command {
        override val name: CommandName
            get() = CommandName.Cd

        sealed interface Target {
            data class Directory(val name: String) : Target

            object Parent : Target
        }
    }

    data class ListFiles(val content: List<Content>) : Command {
        override val name: CommandName
            get() = CommandName.Ls

        sealed interface Content {
            val name: String

            data class File(
                override val name: String, val size: Long
            ) : Content

            data class Directory(override val name: String) : Content
        }
    }

    enum class CommandName(val value: String) {
        Ls("ls"), Cd("cd")
    }

    companion object {
        private val CommandRegex = """\$ (cd|ls)\s?(\.\.|/|\w+)?""".toRegex()
        fun parse(lines: List<String>): List<Command> {
            return buildList {
                val iterator = lines.listIterator()
                while (iterator.hasNext()) {
                    val line = iterator.next()
                    val matchResult = CommandRegex.matchEntire(line) ?: continue
                    val commandName = matchResult.groupValues[1]
                    val args = matchResult.groupValues.getOrNull(2)
                    val command = when (commandName) {
                        CommandName.Ls.value -> {
                            ListFiles(buildList {
                                while (iterator.hasNext()) {
                                    val result = iterator.next()
                                    if (result.matches(CommandRegex)) {
                                        break
                                    }
                                    val (sizeOrDir, name) = result.split(" ")
                                    val content = when (sizeOrDir) {
                                        "dir" -> ListFiles.Content.Directory(name)
                                        else -> ListFiles.Content.File(name, sizeOrDir.toLong())
                                    }
                                    add(content)
                                }
                                if (iterator.hasNext()) {
                                    iterator.previous()
                                }
                            })
                        }

                        CommandName.Cd.value -> {
                            when (args) {
                                null -> null
                                ".." -> ChangeDirectory.Target.Parent
                                else -> ChangeDirectory.Target.Directory(args)
                            }?.let { target ->
                                ChangeDirectory(target)
                            }
                        }

                        else -> null
                    }
                    if (command != null) {
                        add(command)
                    }
                }
            }
        }
    }
}

sealed interface Content {
    val path: String
    val size: Long

    data class File(
        override val path: String, override val size: Long
    ) : Content

    data class Directory(
        override val path: String, val children: List<Content>, val parent: String?
    ) : Content {
        override val size: Long
            get() = children.sumOf { it.size }
    }
}

fun main() {
    val example = buildFileTree(Command.parse(readInput("example/07.txt").split()))
    val input = buildFileTree(Command.parse(readInput("07.txt").trim().split()))

    requireEquals(95437, directoriesLighterThan(example, 100000).sumOf { it.size })
    println(directoriesLighterThan(input, 100000).sumOf { it.size })

    requireEquals(24933642, directoriesToDelete(example).minOf { it.size })
    println(directoriesToDelete(input).minOf { it.size })

}

fun directoriesToDelete(
    root: Content.Directory,
    totalSpace: Long = 70000000,
    neededSpace: Long = 30000000
): List<Content.Directory> {
    val availableSpace = totalSpace - root.size
    fun MutableList<Content.Directory>.directoriesToDelete(current: Content.Directory) {
        current.children.filterIsInstance<Content.Directory>()
            .onEach(::directoriesToDelete)
        if (availableSpace + current.size > neededSpace) {
            add(current)
        }
    }

    return buildList {
        directoriesToDelete(root)
    }
}

fun directoriesLighterThan(root: Content.Directory, maxSize: Long): List<Content.Directory> {
    fun MutableList<Content.Directory>.directoryLighterThan(
        currentDirectory: Content.Directory,
        maxSize: Long
    ) {
        currentDirectory.children.filterIsInstance<Content.Directory>()
            .onEach { content -> directoryLighterThan(content, maxSize) }
        if (currentDirectory.size < maxSize) {
            add(currentDirectory)
        }
    }
    return buildList {
        directoryLighterThan(root, maxSize)
    }
}

private fun buildFileTree(commands: List<Command>): Content.Directory {
    val fileTree = mutableMapOf<String, Content>(
        "/" to Content.Directory(path = "/", children = emptyList(), parent = null)
    )
    var currentPath = ""
    var parentPath: String? = null
    commands.forEach { command ->
        when (command) {
            is Command.ChangeDirectory -> {
                when (command.target) {
                    is Command.ChangeDirectory.Target.Directory -> {
                        parentPath = currentPath
                        currentPath = path(name = command.target.name, parentPath = currentPath)
                        fileTree.computeIfPresent(currentPath) { _, file ->
                            if (file is Content.Directory) {
                                file.copy(parent = parentPath)
                            } else {
                                file
                            }
                        }
                    }

                    Command.ChangeDirectory.Target.Parent -> {
                        parentPath?.let { path ->
                            parentPath = (fileTree[path] as? Content.Directory)?.parent
                            currentPath = path
                        }
                    }
                }
            }

            is Command.ListFiles -> {
                val files = command.content.map { content ->
                    when (content) {
                        is Command.ListFiles.Content.Directory -> {
                            Content.Directory(
                                path = path(parentPath = currentPath, name = content.name),
                                parent = currentPath,
                                children = emptyList()
                            )
                        }

                        is Command.ListFiles.Content.File -> {
                            Content.File(
                                path = path(parentPath = currentPath, name = content.name), size = content.size
                            )
                        }
                    }
                }
                fileTree.putAll(files.associateBy { it.path })
                fileTree.computeIfPresent(currentPath) { _, file ->
                    if (file is Content.Directory) {
                        file.copy(children = files)
                    } else {
                        file
                    }
                }
            }
        }
    }
    fileTree.walkAndUpdate("/")
    return fileTree.getValue("/") as Content.Directory
}

private fun MutableMap<String, Content>.walkAndUpdate(currentPath: String): Content {
    return computeIfPresent(currentPath) { _, content ->
        when (content) {
            is Content.Directory -> {
                val updatedChildren = content.children.map { child ->
                    when (child) {
                        is Content.Directory -> {
                            walkAndUpdate(child.path)
                        }

                        is Content.File -> child
                    }
                }
                content.copy(children = updatedChildren)
            }

            is Content.File -> content
        }
    } ?: error("Missing $currentPath")
}

private fun path(parentPath: String, name: String): String {
    return if (name == "/") {
        name
    } else if (parentPath == "/") {
        "/${name}"
    } else {
        "$parentPath/${name}"
    }
}