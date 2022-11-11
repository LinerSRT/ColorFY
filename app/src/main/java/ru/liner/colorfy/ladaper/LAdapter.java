package ru.liner.colorfy.ladaper;

import android.util.SparseArray;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.liner.colorfy.core.WallpaperData;
import ru.liner.colorfy.listener.IWallpaperDataListener;

@SuppressWarnings("ALL")
public class LAdapter extends RecyclerView.Adapter<LAdapter.ViewHolder> {
    private final List<Object> itemsList = new ArrayList<>();
    private final SparseArray<Class> binderList = new SparseArray<>();
    private final SparseArray<Class> layoutsList = new SparseArray<>();
    private final Map<Class, Integer> typesList = new HashMap<>();
    private final Map<Class, LTouchListener> lTouchListenerHashMap = new HashMap<>();


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Class binderClass = binderList.get(viewType);
        if (binderClass != null) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            try {
                LBinder LBinder = (LBinder) binderClass.newInstance();
                LBinder.setItemView(itemView);
                return new ViewHolder(itemView, LBinder, layoutsList.get(viewType));
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        throw new IllegalStateException("No binder added for viewType " + viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(itemsList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public int indexOf(@NonNull Object o) {
        return itemsList.indexOf(o);
    }

    public void remove(int index) {
        itemsList.remove(index);
        notifyItemRemoved(index);
    }

    public void insert(int index, @NonNull Object o) {
        itemsList.add(index, o);
        notifyItemInserted(index);
    }

    public void add(@NonNull Object item) {
        itemsList.add(item);
        notifyItemInserted(itemsList.size()-1);
    }

    public void add(@NonNull Object... items) {
        int position = itemsList.size();
        itemsList.addAll(Arrays.asList(items));
        notifyItemInserted(position);
    }

    public void add(@NonNull Collection<?> items) {
        int position = itemsList.size();
        itemsList.addAll(items);
        notifyItemInserted(position);
    }

    public void set(@NonNull Object... items) {
        itemsList.clear();
        itemsList.addAll(Arrays.asList(items));
        notifyDataSetChanged();
    }

    public void set(@NonNull Collection<?> items) {
        itemsList.clear();
        itemsList.addAll(items);
        notifyDataSetChanged();
    }

    public void swap(int from, int to){
        if (from < to) {
            for (int i = from; i < to; i++)
                Collections.swap(itemsList, i, i + 1);
        } else {
            for (int i = from; i > to; i--)
                Collections.swap(itemsList, i, i - 1);
        }
        notifyItemMoved(from, to);
    }

    public void shuffle(){
        Collections.shuffle(itemsList);
        notifyDataSetChanged();
    }

    public void clear() {
        itemsList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    public <T> T get(int i) {
        return (T) itemsList.get(i);
    }

    public <T> List<T> getItemsList() {
        return (List<T>) itemsList;
    }

    @Override
    public int getItemViewType(int position) {
        Object o = itemsList.get(position);
        if (typesList.containsKey(o.getClass())) {
            return typesList.get(o.getClass());
        } else {
            throw new IllegalStateException("Class " + o.getClass().getSimpleName() + " not registered in the adapter");
        }
    }

    public <R extends LBinder<T>, T> LAdapter register(@LayoutRes int layout, @NonNull Class<T> clazz, @NonNull Class<R> binder) {
        return register(layout, clazz, binder, null);
    }

    public <R extends LBinder<T>, T> LAdapter register(@LayoutRes int layout, @NonNull Class<T> clazz, @NonNull Class<R> binder, LTouchListener<R, T> touchListener) {
        binderList.put(layout, binder);
        typesList.put(clazz, layout);
        layoutsList.put(layout, clazz);
        if (touchListener != null)
            lTouchListenerHashMap.put(clazz, touchListener);
        return this;
    }

    class ViewHolder<R extends LBinder<T>, T> extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnDragListener, IWallpaperDataListener {
        @NonNull
        private final Class<T> modelClass;
        private final R binder;
        private T model = null;

        ViewHolder(@NonNull View itemView, R binder, @NonNull Class<T> modelClass) {
            super(itemView);
            this.modelClass = modelClass;
            this.binder = binder;
            this.binder.init();
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setOnDragListener(this);
        }

        void bind(T model) {
            this.model = model;
            binder.bind(model);
        }

        @Override
        public void onClick(@NonNull View view) {
            if (lTouchListenerHashMap.containsKey(modelClass)) {
                lTouchListenerHashMap.get(modelClass).onTouch(binder, model);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (lTouchListenerHashMap.containsKey(modelClass)) {
                lTouchListenerHashMap.get(modelClass).onLongTouch(binder, model);
            }
            return true;
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            if (lTouchListenerHashMap.containsKey(modelClass)) {
                lTouchListenerHashMap.get(modelClass).onDrag(binder, event, model);
            }
            return false;
        }

        public boolean allowDrag() {
            return binder.getDragDirections() != 0;
        }

        public int getDragDirections() {
            return binder.getDragDirections();
        }

        public boolean allowSwipe() {
            return binder.getSwipeDirections() != 0;
        }

        public int getSwipeDirections() {
            return binder.getSwipeDirections();
        }

        @Override
        public void onChanged(@NonNull WallpaperData wallpaperData) {
            binder.onChanged(wallpaperData);
        }
    }

}
