package keyboard.cta

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import keyboard.cta.databinding.ActivityMainBinding
import keyboard.cta.databinding.ItemOneBinding
import keyboard.cta.databinding.ItemThreeBinding
import keyboard.cta.databinding.ItemTwoBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpLayout()
    }

    private fun setUpLayout() {
        val adapter = MyAdapter(binding)
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            isNestedScrollingEnabled = true
        }
        binding.recyclerview.adapter = adapter

        val list = listOf(
            GeneralDataItem(type = ViewType.ONE),
            GeneralDataItem(type = ViewType.ONE),
            GeneralDataItem(type = ViewType.THREE),
            GeneralDataItem(type = ViewType.ONE),
            GeneralDataItem(type = ViewType.ONE),
            GeneralDataItem(type = ViewType.ONE),
            GeneralDataItem(type = ViewType.THREE),
            GeneralDataItem(type = ViewType.ONE),
            GeneralDataItem(type = ViewType.THREE),
            GeneralDataItem(type = ViewType.TWO, showCta = true),
            GeneralDataItem(type = ViewType.THREE),
            GeneralDataItem(type = ViewType.ONE),
            GeneralDataItem(type = ViewType.ONE),
            GeneralDataItem(type = ViewType.THREE),
            GeneralDataItem(type = ViewType.ONE),
            GeneralDataItem(type = ViewType.ONE)
        )

        adapter.submitList(list)

    }

    class MyAdapter(val activityMainBinding: ActivityMainBinding) : ListAdapter<GeneralDataItem, MyAdapter.GeneralViewHolder>(ItemDiffUtil) {
        companion object {
            private object ItemDiffUtil : DiffUtil.ItemCallback<GeneralDataItem>() {
                override fun areItemsTheSame(oldItem: GeneralDataItem, newItem: GeneralDataItem): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(oldItem: GeneralDataItem, newItem: GeneralDataItem): Boolean {
                    return oldItem == newItem
                }
            }
        }

        override fun getItemCount(): Int = currentList.size

        override fun getItemViewType(position: Int): Int = currentList[position].type.ordinal

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeneralViewHolder {
            return when (ViewType.from(viewType)) {
                ViewType.ONE -> {
                    ItemOneViewHolder(ItemOneBinding.inflate(LayoutInflater.from(parent.context), parent, false))
                }

                ViewType.TWO -> {
                    ItemTwoViewHolder(ItemTwoBinding.inflate(LayoutInflater.from(parent.context), parent, false), activityMainBinding)
                }

                ViewType.THREE -> {
                    ItemThreeViewHolder(ItemThreeBinding.inflate(LayoutInflater.from(parent.context), parent, false))

                }
            }
        }

        override fun onBindViewHolder(holder: GeneralViewHolder, position: Int) {
            holder.bind(currentList[holder.adapterPosition])
        }

        abstract class GeneralViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            abstract fun bind(item: GeneralDataItem)
        }


        class ItemOneViewHolder(val binding: ItemOneBinding) : GeneralViewHolder(binding.root) {
            override fun bind(item: GeneralDataItem) {

            }
        }

        inner class ItemTwoViewHolder(val binding: ItemTwoBinding, val activityMainBinding: ActivityMainBinding) :
            GeneralViewHolder(binding.root) {
            override fun bind(item: GeneralDataItem) {
                binding.chipCta.setOnClickListener {
                    binding.etAmount.setText("4200.56")
                    binding.chipCta.visibility = View.GONE
                }

                binding.etAmount.setOnFocusChangeListener { _, b ->
                    if (item.showCta && b) {
                        binding.chipCta.visibility = View.VISIBLE
                        Handler(Looper.getMainLooper()).postDelayed(
                            {
                                //Take care with this line I think that the scroll moves in px and not with dp.
                                activityMainBinding.recyclerview.scrollBy(0, 160)
                            },
                            300
                        ) //You need to play with the time if your activity comes from onCreate and onResume you need to change this value to 1000.
                    }
                }

                binding.etQuantity.setOnFocusChangeListener { _, b ->
                    binding.chipCta.visibility = View.GONE
                }
            }
        }

        class ItemThreeViewHolder(val binding: ItemThreeBinding) : MyAdapter.GeneralViewHolder(binding.root) {
            override fun bind(item: GeneralDataItem) {

            }
        }

    }


    data class GeneralDataItem(val showCta: Boolean = false, val type: ViewType)

    enum class ViewType {
        ONE,
        TWO,
        THREE;

        companion object {
            fun from(value: Int): ViewType {
                return values()[value]
            }
        }
    }
}