package asso.model.knowledgeSources.generators

import asso.model.Blackboard
import asso.model.knowledgeSources.KnowledgeSource
import asso.model.objects.ProcessingStage

abstract class Generator[I](blackboard: Blackboard[I], chain: Vector[ProcessingStage]) extends KnowledgeSource(blackboard)
