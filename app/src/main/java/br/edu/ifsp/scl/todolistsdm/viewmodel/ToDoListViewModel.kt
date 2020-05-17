package br.edu.ifsp.scl.todolistsdm.viewmodel


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.room.Room
import br.edu.ifsp.scl.todolistsdm.model.dao.TarefaDao
import br.edu.ifsp.scl.todolistsdm.model.database.ToDoListDatabase
import br.edu.ifsp.scl.todolistsdm.model.entity.Tarefa
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/* Contexto é passado para poder criar o Room */
class ToDoListViewModel(contexto: Context): ViewModel() {

    private val model: TarefaDao = Room.databaseBuilder(
        contexto,
        ToDoListDatabase::class.java,
        ToDoListDatabase.Constantes.DB_NAME
    ).build().getTarefaDao()

    fun buscarTarefas(): LiveData<MutableList<Tarefa>> {
        return model.recuperarTarefas()
    }

    fun buscarTarefa(tarefaId: Int): LiveData<Tarefa> {
        return model.recuperarTarefa(tarefaId)
    }

    fun apagarTarefa(tarefa: Tarefa){
        GlobalScope.launch { model.removerTarefa(tarefa) }
    }

    fun apagarTarefas(vararg tarefa: Tarefa){
        GlobalScope.launch { model.removerTarefas(*tarefa) }
    }

    fun salvarTarefa(tarefa: Tarefa): LiveData<Long> {
        /* Usando a função liveData para chamar uma função suspend e criar um LiveData */
        val tarefaIdLD: LiveData<Long> = liveData {
            emit(model.inserirTarefa(tarefa)) // retorna e atribui o LiveData criado
        }
        return tarefaIdLD
    }

    fun alterarTarefa(tarefa: Tarefa) {
        GlobalScope.launch {
            model.atualizarTarefa(tarefa)
        }
    }
}